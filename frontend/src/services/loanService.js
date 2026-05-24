import API from '../utils/axiosConfig';

export const loanService = {
  applyForLoan: (userId, data) =>
    API.post(`/api/loans/apply?userId=${userId}`, data),
  getUserLoans: (userId) =>
    API.get(`/api/loans/user/${userId}`),
  getAllLoans: () =>
    API.get('/api/loans'),
  getPendingLoans: () =>
    API.get('/api/loans/pending'),
  approveLoan: (loanId, remarks) =>
    API.put(`/api/loans/${loanId}/approve?remarks=${remarks}`),
  rejectLoan: (loanId, remarks) =>
    API.put(`/api/loans/${loanId}/reject?remarks=${remarks}`),
};