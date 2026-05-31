import { useEffect, useState } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import { logout } from '../store/slices/authSlice'
import { setAccounts } from '../store/slices/accountSlice'
import { accountService } from '../services/accountService'

function Dashboard() {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const { user } = useSelector((state) => state.auth)
  const { accounts } = useSelector((state) => state.account)
  const [loading, setLoading] = useState(true)
  const [selectedAccount, setSelectedAccount] = useState(null)
  const [action, setAction] = useState(null) // 'deposit', 'withdraw', 'transfer'
  const [amount, setAmount] = useState('')
  const [description, setDescription] = useState('')
  const [toAccountId, setToAccountId] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [processing, setProcessing] = useState(false)

  useEffect(() => {
    loadAccounts()
  }, [])

  const loadAccounts = async () => {
    try {
      const response = await accountService.getUserAccounts(1)
      dispatch(setAccounts(response.data))
    } catch (err) {
      console.error('Failed to load accounts:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleAction = async (e) => {
    e.preventDefault()
    setProcessing(true)
    setMessage('')
    setError('')

    try {
      if (action === 'deposit') {
        await accountService.deposit(selectedAccount.id, {
          amount: parseFloat(amount),
          description
        })
        setMessage(`✅ Successfully deposited $${amount}!`)
      } else if (action === 'withdraw') {
        await accountService.withdraw(selectedAccount.id, {
          amount: parseFloat(amount),
          description
        })
        setMessage(`✅ Successfully withdrew $${amount}!`)
      } else if (action === 'transfer') {
        await accountService.transfer(selectedAccount.id, {
          amount: parseFloat(amount),
          description,
          toAccountId: parseInt(toAccountId)
        })
        setMessage(`✅ Successfully transferred $${amount}!`)
      }

      setAmount('')
      setDescription('')
      setToAccountId('')
      setAction(null)
      loadAccounts() // Refresh balances
    } catch (err) {
      setError(err.response?.data || 'Transaction failed!')
    } finally {
      setProcessing(false)
    }
  }

  const handleLogout = () => {
    dispatch(logout())
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Navbar */}
      <nav className="bg-blue-900 text-white px-6 py-4
                      flex justify-between items-center shadow-lg">
        <h1 className="text-xl font-bold">🏦 BankingApp</h1>
        <div className="flex items-center gap-4">
          <span className="text-blue-200 text-sm">
            Welcome, {user?.fullName || 'User'}!
          </span>
          <button onClick={() => navigate('/loans')}
                  className="bg-blue-700 hover:bg-blue-600
                             px-3 py-2 rounded-lg text-sm transition">
            🏦 Loans
          </button>
          <button onClick={() => navigate('/transactions')}
                  className="bg-blue-700 hover:bg-blue-600
                             px-3 py-2 rounded-lg text-sm transition">
            💸 Transactions
          </button>
          <button onClick={handleLogout}
                  className="bg-red-600 hover:bg-red-500
                             px-3 py-2 rounded-lg text-sm transition">
            Logout
          </button>
        </div>
      </nav>

      <div className="max-w-6xl mx-auto p-6">

        {/* Success/Error Messages */}
        {message && (
          <div className="bg-green-50 border border-green-200 text-green-700
                          rounded-lg p-4 mb-4">
            {message}
          </div>
        )}
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-600
                          rounded-lg p-4 mb-4">
            {error}
          </div>
        )}

        {/* Accounts */}
        <h2 className="text-2xl font-bold text-gray-800 mb-4">
          My Accounts
        </h2>

        {loading ? (
          <div className="text-center py-10 text-gray-500">
            Loading accounts...
          </div>
        ) : accounts.length === 0 ? (
          <div className="bg-white rounded-xl p-8 text-center shadow">
            <p className="text-gray-500">No accounts yet!</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
            {accounts.map((account) => (
              <div key={account.id}
                   className="bg-white rounded-xl shadow p-6
                              border-l-4 border-blue-500">
                <div className="flex justify-between items-start">
                  <div>
                    <p className="text-sm text-gray-500">
                      {account.accountType} Account
                    </p>
                    <p className="text-xs text-gray-400 mt-1">
                      {account.accountNumber}
                    </p>
                  </div>
                  <span className={`text-xs px-2 py-1 rounded-full
                    ${account.status === 'ACTIVE'
                      ? 'bg-green-100 text-green-600'
                      : 'bg-red-100 text-red-600'}`}>
                    {account.status}
                  </span>
                </div>
                <p className="text-3xl font-bold text-blue-900 mt-4">
                  ${parseFloat(account.balance).toLocaleString()}
                </p>

                {/* Action Buttons */}
                <div className="flex gap-2 mt-4">
                  <button
                    onClick={() => {
                      setSelectedAccount(account)
                      setAction('deposit')
                      setMessage('')
                      setError('')
                    }}
                    className="flex-1 bg-green-500 hover:bg-green-600
                               text-white text-sm py-2 rounded-lg transition">
                    💰 Deposit
                  </button>
                  <button
                    onClick={() => {
                      setSelectedAccount(account)
                      setAction('withdraw')
                      setMessage('')
                      setError('')
                    }}
                    className="flex-1 bg-orange-500 hover:bg-orange-600
                               text-white text-sm py-2 rounded-lg transition">
                    💸 Withdraw
                  </button>
                  <button
                    onClick={() => {
                      setSelectedAccount(account)
                      setAction('transfer')
                      setMessage('')
                      setError('')
                    }}
                    className="flex-1 bg-purple-500 hover:bg-purple-600
                               text-white text-sm py-2 rounded-lg transition">
                    🔄 Transfer
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Transaction Form */}
        {action && selectedAccount && (
          <div className="bg-white rounded-xl shadow p-6 mb-6">
            <h3 className="text-lg font-bold text-gray-800 mb-4">
              {action === 'deposit' && '💰 Deposit Money'}
              {action === 'withdraw' && '💸 Withdraw Money'}
              {action === 'transfer' && '🔄 Transfer Money'}
              <span className="text-sm font-normal text-gray-500 ml-2">
                ({selectedAccount.accountNumber})
              </span>
            </h3>

            <form onSubmit={handleAction} className="space-y-4">
              <div>
                <label className="block text-sm font-medium
                                  text-gray-700 mb-1">
                  Amount ($)
                </label>
                <input
                  type="number"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  required
                  min="1"
                  className="w-full border border-gray-300 rounded-lg
                             px-4 py-3 focus:outline-none
                             focus:ring-2 focus:ring-blue-500"
                  placeholder="Enter amount"
                />
              </div>

              {action === 'transfer' && (
                <div>
                  <label className="block text-sm font-medium
                                    text-gray-700 mb-1">
                    To Account ID
                  </label>
                  <input
                    type="number"
                    value={toAccountId}
                    onChange={(e) => setToAccountId(e.target.value)}
                    required
                    className="w-full border border-gray-300 rounded-lg
                               px-4 py-3 focus:outline-none
                               focus:ring-2 focus:ring-blue-500"
                    placeholder="Enter account ID"
                  />
                </div>
              )}

              <div>
                <label className="block text-sm font-medium
                                  text-gray-700 mb-1">
                  Description (optional)
                </label>
                <input
                  type="text"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  className="w-full border border-gray-300 rounded-lg
                             px-4 py-3 focus:outline-none
                             focus:ring-2 focus:ring-blue-500"
                  placeholder="e.g. Salary deposit"
                />
              </div>

              <div className="flex gap-3">
                <button
                  type="submit"
                  disabled={processing}
                  className="flex-1 bg-blue-600 hover:bg-blue-700
                             text-white font-semibold py-3 rounded-lg
                             transition disabled:opacity-50">
                  {processing ? 'Processing...' : 'Confirm'}
                </button>
                <button
                  type="button"
                  onClick={() => setAction(null)}
                  className="flex-1 bg-gray-200 hover:bg-gray-300
                             text-gray-700 font-semibold py-3
                             rounded-lg transition">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}
      </div>
    </div>
  )
}

export default Dashboard