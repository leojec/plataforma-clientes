import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import toast from 'react-hot-toast';

function StatusModal({ isOpen, onClose, onSave, leadId, currentStatus }) {
  const [selectedStatus, setSelectedStatus] = useState('');

  const statusOptions = [
    { value: 'Lead', label: 'Lead' },
    { value: 'Em Andamento', label: 'Em Andamento' },
    { value: 'Em Negociação', label: 'Em Negociação' },
    { value: 'Stand Fechado', label: 'Stand Fechado' }
  ];


  useEffect(() => {
    if (isOpen) {
      setSelectedStatus('');
    }
  }, [isOpen]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!selectedStatus) {
      toast.error('Por favor, selecione um status.');
      return;
    }

    const statusData = {
      leadId,
      newStatus: selectedStatus,
      previousStatus: currentStatus,
      timestamp: new Date().toISOString(),
    };

    onSave(statusData);
    onClose();
  };

  const handleCancel = () => {
    setSelectedStatus('');
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50 flex justify-center items-center">
      <div className="relative p-6 border w-full max-w-md shadow-lg rounded-md bg-white mx-4">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-xl font-bold text-gray-900">Alterar Status</h3>
          <button onClick={handleCancel} className="text-gray-400 hover:text-gray-600">
            <X className="h-6 w-6" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="status" className="block text-sm font-medium text-gray-700 mb-2">
              Selecione um Status
            </label>
            <select
              id="status"
              name="status"
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
            >
              <option value="">Selecione um Status</option>
              {statusOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>

          <div className="flex space-x-3 pt-4">
            <button
              type="button"
              onClick={handleCancel}
              className="flex-1 bg-red-600 hover:bg-red-700 text-white font-medium py-2 px-4 rounded-md transition-colors"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md transition-colors"
            >
              Alterar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default StatusModal;
