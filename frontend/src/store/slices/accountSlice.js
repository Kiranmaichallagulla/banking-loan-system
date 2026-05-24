import { createSlice } from '@reduxjs/toolkit';

const accountSlice = createSlice({
  name: 'account',
  initialState: {
    accounts: [],
    selectedAccount: null,
    transactions: [],
  },
  reducers: {
    setAccounts: (state, action) => {
      state.accounts = action.payload;
    },
    setSelectedAccount: (state, action) => {
      state.selectedAccount = action.payload;
    },
    setTransactions: (state, action) => {
      state.transactions = action.payload;
    },
  },
});

export const {
  setAccounts,
  setSelectedAccount,
  setTransactions
} = accountSlice.actions;
export default accountSlice.reducer;