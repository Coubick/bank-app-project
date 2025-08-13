package org.example.services;

import org.example.enums.EventTypes;
import org.example.account_dao.Account;
import org.example.account_dao.AccountRepository;
import org.example.enums.AddFindDeleteResult;
import org.example.enums.DepositResult;
import org.example.enums.TransferResult;
import org.example.enums.WithdrawResult;
import org.example.operations_dao.OperationClass;
import org.example.operations_dao.OperationRepository;
import org.example.producers.AccountEventProducer;
import org.example.user_dao.User;
import org.example.user_dao.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис для счёта
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final BigDecimal BALANCE_DOES_NOT_EXISTS = BigDecimal.valueOf(-1);
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final UserRepository userRepository;
    private final AccountEventProducer accountEventProducer;

    public AccountServiceImpl(AccountRepository accountRepository, OperationRepository operationRepository, UserRepository userRepository, AccountEventProducer accountEventProducer) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
        this.userRepository = userRepository;
        this.accountEventProducer = accountEventProducer;
    }

    @Override
    public AddFindDeleteResult addAccount(Account account) {
        User user = userRepository.findByLogin(account.getOwnerLogin());
        if (user != null) {
            accountRepository.save(account);
            accountEventProducer.sendAccountEvent(Integer.toString(account.getUserDefinedId()), account, String.valueOf(EventTypes.CREATED));
            return AddFindDeleteResult.Success;
        }
        return AddFindDeleteResult.AlreadyExists;
    }

    @Override
    public boolean accountExists(int id) {
        return accountRepository.findByUserDefinedId(id) != null;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public DepositResult deposit(int id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(0)) > 0) {
            Account account = accountRepository.findByUserDefinedId(id);
            if (account == null) {
                return DepositResult.AccountDoesNotExist;
            }

            account.setBalance(account.getBalance().add(amount));
            accountEventProducer.sendAccountEvent(Integer.toString(account.getUserDefinedId()), account, String.valueOf(EventTypes.DEPOSIT));
            accountRepository.save(account);

            OperationClass operation = new OperationClass();
            operation.setAccountId(id);
            operation.setOperationType("DEPOSIT");
            operation.setMoneyAmount(amount);
            operationRepository.save(operation);

            return DepositResult.Success;
        }
        return DepositResult.IncorrectAmount;
    }

    @Override
    public WithdrawResult withdraw(int id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
            return WithdrawResult.IncorrectAmount;
        }

        try {
            Account acc = accountRepository.findByUserDefinedId(id);
            if (acc == null)
                return WithdrawResult.AccountNotFound;
            else{
            BigDecimal currBalance = accountRepository.findByUserDefinedId(id).getBalance();
            if (currBalance == null) {
                return WithdrawResult.AccountNotFound;
            }

            if (currBalance.compareTo(amount) < 0) {
                return WithdrawResult.InsufficientFunds;
            }


            Account account = accountRepository.findByUserDefinedId(id);
            account.setBalance(currBalance.subtract(amount));
            accountEventProducer.sendAccountEvent(Integer.toString(account.getUserDefinedId()), account, String.valueOf(EventTypes.WITHDRAW));
            accountRepository.save(account);

            OperationClass operation = new OperationClass();
            operation.setOperationType("WITHDRAW");
            operation.setAccountId(id);
            operation.setMoneyAmount(amount);
            operationRepository.save(operation);

            return WithdrawResult.Success;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return WithdrawResult.Error;
        }
    }

    @Override
    public TransferResult transfer(int senderAccountId, int targetAccountId, double amount) {
        if (amount <= 0) return TransferResult.NotEnoughMoney;

        Account senderAccount = accountRepository.findByUserDefinedId(senderAccountId);
        Account targetAccount = accountRepository.findByUserDefinedId(targetAccountId);

        if (senderAccount == null) return TransferResult.SenderAccountDoesNotExist;
        if (targetAccount == null) return TransferResult.TargetAccountDoesNotExist;

        String senderLogin = senderAccount.getOwnerLogin();
        String targetLogin = targetAccount.getOwnerLogin();
        BigDecimal totalAmount = BigDecimal.valueOf(amount);

        BigDecimal totalWithCommission = commissionCalc(senderLogin, targetLogin, amount);

        if (senderAccount.getBalance().compareTo(totalWithCommission) < 0) {
            return TransferResult.NotEnoughMoney;
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(totalWithCommission));
        targetAccount.setBalance(targetAccount.getBalance().add(totalAmount));

        accountRepository.save(senderAccount);
        accountEventProducer.sendAccountEvent(Integer.toString(senderAccount.getUserDefinedId()), senderAccount, String.valueOf(EventTypes.TRANSFER_SENT));
        accountRepository.save(targetAccount);
        accountEventProducer.sendAccountEvent(Integer.toString(targetAccount.getUserDefinedId()), targetAccount, String.valueOf(EventTypes.TRANSFER_RECEIVED));

        OperationClass sent = new OperationClass();
        sent.setAccountId(senderAccountId);
        sent.setOperationType("TRANSFER SENT");
        sent.setMoneyAmount(totalWithCommission);
        operationRepository.save(sent);

        OperationClass received = new OperationClass();
        received.setAccountId(targetAccountId);
        received.setOperationType("TRANSFER RECEIVED");
        received.setMoneyAmount(totalAmount);
        operationRepository.save(received);

        return TransferResult.Success;
    }

    @Override
    public BigDecimal checkBalance(int id) {
        Account acc = accountRepository.findByUserDefinedId(id);
        if (acc != null)
            return accountRepository.findByUserDefinedId(id).getBalance();
        return BALANCE_DOES_NOT_EXISTS;
    }

    private BigDecimal commissionCalc(String senderLogin, String targetLogin, double amount){
        BigDecimal totalAmount = BigDecimal.valueOf(amount);
        double commissionRate = 0;
        if (!senderLogin.equals(targetLogin)) {
            User user  = userRepository.findByLogin(targetLogin);
            User friend = userRepository.findByLogin(senderLogin);
            List<User> userFriends = user.getFriends();
            List<User> friendFriends = friend.getFriends();
            commissionRate = userFriends.contains(friend) && friendFriends.contains(user) ? 0.03 : 0.1;
        }

        BigDecimal commission = totalAmount.multiply(BigDecimal.valueOf(commissionRate));
        BigDecimal totalWithCommission = totalAmount.add(commission);
        return totalWithCommission;
    }
}