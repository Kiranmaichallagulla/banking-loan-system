import API from '../utils/axiosConfig';

export const accountService = {
  createAccount: (userId, type) =>
    API.post(`/api/accounts/create?userId=${userId}&type=${type}`),
  getUserAccounts: (userId) =>
    API.get(`/api/accounts/user/${userId}`),
  deposit: (accountId, data) =>
    API.post(`/api/accounts/${accountId}/deposit`, data),
  withdraw: (accountId, data) =>
    API.post(`/api/accounts/${accountId}/withdraw`, data),
  transfer: (accountId, data) =>
    API.post(`/api/accounts/${accountId}/transfer`, data),
  getTransactions: (accountId) =>
    API.get(`/api/accounts/${accountId}/transactions`),
};