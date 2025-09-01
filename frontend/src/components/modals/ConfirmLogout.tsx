import React, { useState } from 'react';

interface ConfirmLogoutProps {
  
  onClose: () => void;
  onConfirm: () => void;
}

export default function ConfirmLogout({
  onClose,
  onConfirm,
}: ConfirmLogoutProps) {
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleLogout = async () => {
    setIsSubmitting(true);
    try {
      onConfirm();
    } catch (error) {
      console.error(`Failed to logout:`, error);
    } finally {
      setIsSubmitting(false);
      onClose();
    }
  };

  return (
   <div className="bg-white p-4 rounded shadow-md w-full max-w-sm text-center">
      <h2 className="text-xl font-bold mb-4 text-red-600">Logging out</h2>
      <p className="mb-4">
        Are you sure you want to logout?
      </p>
      <div className="flex justify-end gap-4">
        <button
          onClick={onClose}
          className="btn-base btn-gray"
          disabled={isSubmitting}
        >
          Cancel
        </button>
        <button
          onClick={handleLogout}
          className="btn-base btn-red"
          disabled={isSubmitting}
        >
          Logout
        </button>
      </div>
    </div>
  );
}
