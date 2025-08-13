package org.example.services;

import org.example.account_dao.Account;
import org.example.enums.AddFindDeleteResult;
import org.example.enums.DepositResult;
import org.example.enums.TransferResult;
import org.example.enums.WithdrawResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис для счёта
 */
  public interface AccountService {
  /**
   * Пополнение счёта
   * @param id     - ID счёта, на который нужно положить деньги
   * @param amount - количество денег, которые нужно положить
   * @return - результат пополнения
   */
  DepositResult deposit(int id, BigDecimal amount);

  /**
   * Снятие денег со счета
   * @param amount - кол-во денег для снятия
   * @return - результат снятия
   */
  WithdrawResult withdraw(int id, BigDecimal amount);

  /**
   * Перевод с одного счёта на другой
   * @param senderAccountId - ID счета, с которого надо перевести деньги
   * @param targetAccountId - ID счета, на который нужно перевести деньги
   * @param amount          - количество денег, которые надо перевести
   * @return результат перевода
   */
  TransferResult transfer(int senderAccountId, int targetAccountId, double amount);

  /**
   * Проверка баланса
   * @param id - ID счёта, на котором нужно проверить баланс
   * @return возвращает баланс счёта
   */
  BigDecimal checkBalance(int id);

  /**
   * @param account - счёт, который нужно добавить в БД
   * @return результат добавления
   */
  AddFindDeleteResult addAccount(Account account);

  /**
   * Проверка на существование счёта
   * @param id - идентификатор счёта, существование которого надо проверить
   * @return true/false
   */
  boolean accountExists(int id);

  List<Account> getAllAccounts();
}