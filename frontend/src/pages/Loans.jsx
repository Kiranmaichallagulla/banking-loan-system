import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { loanService } from '../services/loanService'

function Loans() {
  const navigate = useNavigate()
  const [loans, setLoans] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    amount: '',
    purpose: '',
    termMonths: ''
  })
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [processing, setProcessing] = useState(false)

  useEffect(() => {
    loadLoans()
  }, [])

  const loadLoans = async () => {
    try {
      const response = await loanService.getUserLoans(1)
      setLoans(response.data)
    } catch (err) {
      console.error('Failed to load loans:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleApply = async (e) => {
    e.preventDefault()
    setProcessing(true)
    setError('')
    setMessage('')

    try {
      await loanService.applyForLoan(1, {
        amount: parseFloat(formData.amount),
        purpose: formData.purpose,
        termMonths: parseInt(formData.termMonths)
      })
      setMessage('✅ Loan application submitted successfully!')
      setShowForm(false)
      setFormData({ amount: '', purpose: '', termMonths: '' })
      loadLoans()
    } catch (err) {
      setError(err.response?.data || 'Loan application failed!')
    } finally {
      setProcessing(false)
    }
  }

  const getStatusColor = (status) => {
    switch (status) {
      case 'APPROVED': return 'bg-green-100 text-green-600'
      case 'REJECTED': return 'bg-red-100 text-red-600'
      default: return 'bg-yellow-100 text-yellow-600'
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

        {/* Header */}
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">My Loans</h2>
          <button
            onClick={() => setShowForm(!showForm)}
            className="bg-blue-600 hover:bg-blue-700 text-white
                       px-4 py-2 rounded-lg text-sm font-medium transition">
            {showForm ? 'Cancel' : '+ Apply for Loan'}
          </button>
        </div>

        {/* Messages */}
        {message && (
          <div className="bg-green-50 border border-green-200
                          text-green-700 rounded-lg p-4 mb-4">
            {message}
          </div>
        )}
        {error && (
          <div className="bg-red-50 border border-red-200
                          text-red-600 rounded-lg p-4 mb-4">
            {error}
          </div>
        )}

        {/* Apply Form */}
        {showForm && (
          <div className="bg-white rounded-xl shadow p-6 mb-6">
            <h3 className="text-lg font-bold text-gray-800 mb-4">
              Apply for a Loan
            </h3>
            <form onSubmit={handleApply} className="space-y-4">
              <div>
                <label className="block text-sm font-medium
                                  text-gray-700 mb-1">
                  Loan Amount ($)
                </label>
                <input
                  type="number"
                  value={formData.amount}
                  onChange={(e) => setFormData({
                    ...formData, amount: e.target.value
                  })}
                  required
                  min="1000"
                  className="w-full border border-gray-300 rounded-lg
                             px-4 py-3 focus:outline-none
                             focus:ring-2 focus:ring-blue-500"
                  placeholder="Minimum $1,000"
                />
              </div>

              <div>
                <label className="block text-sm font-medium
                                  text-gray-700 mb-1">
                  Purpose
                </label>
                <input
                  type="text"
                  value={formData.purpose}
                  onChange={(e) => setFormData({
                    ...formData, purpose: e.target.value
                  })}
                  required
                  className="w-full border border-gray-300 rounded-lg
                             px-4 py-3 focus:outline-none
                             focus:ring-2 focus:ring-blue-500"
                  placeholder="e.g. Home renovation"
                />
              </div>

              <div>
                <label className="block text-sm font-medium
                                  text-gray-700 mb-1">
                  Term (months)
                </label>
                <select
                  value={formData.termMonths}
                  onChange={(e) => setFormData({
                    ...formData, termMonths: e.target.value
                  })}
                  required
                  className="w-full border border-gray-300 rounded-lg
                             px-4 py-3 focus:outline-none
                             focus:ring-2 focus:ring-blue-500">
                  <option value="">Select term</option>
                  <option value="6">6 months</option>
                  <option value="12">12 months</option>
                  <option value="24">24 months</option>
                  <option value="36">36 months</option>
                  <option value="60">60 months</option>
                </select>
              </div>

              <button
                type="submit"
                disabled={processing}
                className="w-full bg-blue-600 hover:bg-blue-700
                           text-white font-semibold py-3 rounded-lg
                           transition disabled:opacity-50">
                {processing ? 'Submitting...' : 'Submit Application'}
              </button>
            </form>
          </div>
        )}

        {/* Loans List */}
        {loading ? (
          <div className="text-center py-10 text-gray-500">
            Loading loans...
          </div>
        ) : loans.length === 0 ? (
          <div className="bg-white rounded-xl p-8 text-center shadow">
            <p className="text-gray-500 mb-4">No loan applications yet!</p>
            <button
              onClick={() => setShowForm(true)}
              className="bg-blue-600 text-white px-6 py-2
                         rounded-lg text-sm">
              Apply for your first loan
            </button>
          </div>
        ) : (
          <div className="space-y-4">
            {loans.map((loan) => (
              <div key={loan.id}
                   className="bg-white rounded-xl shadow p-6">
                <div className="flex justify-between items-start">
                  <div>
                    <p className="font-semibold text-gray-800">
                      ${parseFloat(loan.amount).toLocaleString()}
                    </p>
                    <p className="text-sm text-gray-500 mt-1">
                      {loan.purpose}
                    </p>
                    <p className="text-xs text-gray-400 mt-1">
                      Term: {loan.termMonths} months
                    </p>
                  </div>
                  <span className={`text-xs px-3 py-1 rounded-full
                                   font-medium ${getStatusColor(loan.status)}`}>
                    {loan.status}
                  </span>
                </div>
                {loan.adminRemarks && (
                  <div className="mt-3 bg-gray-50 rounded-lg p-3">
                    <p className="text-xs text-gray-500">
                      Admin remarks: {loan.adminRemarks}
                    </p>
                  </div>
                )}
                <p className="text-xs text-gray-400 mt-3">
                  Applied: {new Date(loan.appliedAt).toLocaleDateString()}
                </p>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default Loans