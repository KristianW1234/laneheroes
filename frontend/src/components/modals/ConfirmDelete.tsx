import React, { useState } from 'react';

interface ConfirmDeleteProps {
  type: string; // e.g. "Hero", "Game", etc.
  id: number;
  name?: string; // Optional display name
  onDelete: (id: number) => Promise<void>;
  onClose: () => void;
  onSuccess?: () => void;
}

export default function ConfirmDelete({
  type,
  id,
  name,
  onDelete,
  onClose,
  onSuccess,
}: ConfirmDeleteProps) {
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleDelete = async () => {
    setIsSubmitting(true);
    try {
      await onDelete(id);
      onSuccess?.();
    } catch (error) {
      console.error(`Failed to delete ${type}:`, error);
    } finally {
      setIsSubmitting(false);
      onClose();
    }
  };

  return (
   <div className="bg-white p-4 rounded shadow-md w-full max-w-sm text-center">
      <h2 className="text-xl font-bold mb-4 text-red-600">Delete {type}</h2>
      <p className="mb-4">
        Are you sure you want to delete {type.toLowerCase()}
        {name ? ` "${name}"` : ''}?
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
          onClick={handleDelete}
          className="btn-base btn-red"
          disabled={isSubmitting}
        >
          {isSubmitting ? "Deleting..." : "Delete"}
        </button>
      </div>
    </div>
  );
}
