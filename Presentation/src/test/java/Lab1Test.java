//import org.example.*;
//import org.example.account_dao.Account;
//import org.example.account_dao.AccountRepository;
//import org.example.enums.DepositResult;
//import org.example.enums.WithdrawResult;
//
//import org.example.user_dao.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ServiceTest {
//    private UserService userService;
//    private AccountService accountService;
//    private AccountRepository accountRepository;
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void setUp() {
//        accountRepository = Mockito.mock(AccountRepository.class);
//        userRepository = Mockito.mock(UserRepository.class);
//        UserService userService;
//        AccountService accountService;
//    }
//
//    @Test
//    void withdraw_Success() {
//        int accountId = 1;
//        Account account = new Account(accountId, "user1");
//        account.setBalance(BigDecimal.valueOf(1000));
//
//        when(accountRepository.findAll()).thenReturn((List<Account>) account);
//
//        WithdrawResult result = accountService.withdraw(accountId, BigDecimal.valueOf(500));
//
//        assertEquals(WithdrawResult.Success, result);
//        assertEquals(500, account.getBalance());
//    }
//
//    @Test
//    void withdraw_NotEnoughMoney() {
//        int accountId = 2;
//        Account account = new Account(accountId, "user2");
//        account.setBalance(BigDecimal.valueOf(300));
//
//        when(accountRepository.findByUserDefinedId(accountId)).thenReturn(account);
//
//        WithdrawResult result = accountService.withdraw(accountId, BigDecimal.valueOf(500));
//
//        assertEquals(WithdrawResult.IncorrectAmount, result);
//        assertEquals(300, account.getBalance());
//    }
//
//    @Test
//    void deposit_Success() {
//        int accountId = 3;
//        Account account = new Account(accountId, "user3");
//        account.setBalance(BigDecimal.valueOf(100));
//
//        when(accountRepository.findByUserDefinedId(accountId)).thenReturn(account);
//
//        DepositResult result = accountService.deposit(accountId, BigDecimal.valueOf(400));
//
//        assertEquals(DepositResult.Success, result);
//        assertEquals(500, account.getBalance());
//    }
//}
