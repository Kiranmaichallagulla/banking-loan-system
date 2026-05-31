import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { accountService } from '../services/accountService'

function Transactions() {
  const navigate = useNavigate()
  const { accounts } = useSelector((state) => state.account)
  const [transactions, setTransactions] = useState([])
  const [loading, setLoading] = useState(true)
  const [selectedAccountId, setSelectedAccountId] = useState('')

  useEffect(() => {
    if (accounts.length > 0) {
      setSelectedAccountId(accounts[0].id)
      loadTransactions(accounts[0].id)
    } else {
      setLoading(false)
    }
  }, [accounts])

  const loadTransactions = async (accountId) => {
    setLoading(true)
    try {
      const response = await accountService.getTransactions(accountId)
      setTransactions(response.data)
    } catch (err) {
      console.error('Failed to load transactions:', err)
    } finally {
      setLoading(false)
    }
  }

  const getTypeColor = (type) => {
    switch (type) {
      case 'DEPOSIT': return 'text-green-600'
      case 'WITHDRAWAL': return 'text-red-600'
      default: return 'text-purple-600'
    }
  }

  const getTypeIcon = (type) => {
    switch (type) {
      case 'DEPOSIT': return '↑'
      case 'WITHDRAWAL': return '↓'
      default: return '↔'
    }
  }

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Navbar */}
      <nav className="bg-blue-900 text-white px-6 py-4
                      flex justify-between items-center shadow-lg">
        <h1 className="text-xl font-bold">🏦 BankingApp</h1>
        <button onClick={() => navigate('/dashboard')}
                className="bg-blue-700 hover:bg-blue-600
                           px-4 py-2 rounded-lg text-sm transition">
          ← Dashboard
        </button>
      </nav>

      <div className="max-w-4xl mx-auto p-6">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">
          Transaction History
        </h2>

        {/* Account Selector */}
        {accounts.length > 1 && (
          <select
            value={selectedAccountId}
            onChange={(e) => {
              setSelectedAccountId(e.target.value)
              loadTransactions(e.target.value)
            }}
            className="w-full border border-gray-300 rounded-lg
                       px-4 py-3 mb-6 focus:outline-none
                       focus:ring-2 focus:ring-blue-500 bg-white">
            {accounts.map((acc) => (
              <option key={acc.id} value={acc.id}>
                {acc.accountNumber} - {acc.accountType}
              </option>
            ))}
          </select>
        )}

        {/* Transactions List */}
        {loading ? (
          <div className="text-center py-10 text-gray-500">
            Loading transactions...
          </div>
        ) : transactions.length === 0 ? (
          <div className="bg-white rounded-xl p-8 text-center shadow">
            <p className="text-gray-500">No transactions yet!</p>
          </div>
        ) : (
          <div className="bg-white rounded-xl shadow overflow-hidden">
            {transactions.map((tx, index) => (
              <div key={tx.id}
                   className={`flex items-center justify-between p-4
                     ${index !== transactions.length - 1
                       ? 'border-b border-gray-100' : ''}`}>
                <div className="flex items-center gap-4">
                  <div className={`w-10 h-10 rounded-full flex items-center
                                  justify-center text-lg font-bold
                    ${tx.type === 'DEPOSIT'
                      ? 'bg-green-100'
                      : tx.type === 'WITHDRAWAL'
                      ? 'bg-red-100'
                      : 'bg-purple-100'}`}>
                    <span className={getTypeColor(tx.type)}>
                      {getTypeIcon(tx.type)}
                    </span>
                  </div>
                  <div>
                    <p className="font-medium text-gray-800 text-sm">
                      {tx.type}
                    </p>
                    <p className="text-xs text-gray-500">
                      {tx.description || 'No description'}
                    </p>
                    <p className="text-xs text-gray-400">
                      {new Date(tx.createdAt).toLocaleString()}
                    </p>
                  </div>
                </div>
                <div className="text-right">
                  <p className={`font-bold ${getTypeColor(tx.type)}`}>
                    {tx.type === 'DEPOSIT' ? '+' : '-'}
                    ${parseFloat(tx.amount).toLocaleString()}
                  </p>
                  <p className="text-xs text-gray-400">
                    Balance: ${parseFloat(tx.balanceAfter).toLocaleString()}
                  </p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default Transactions