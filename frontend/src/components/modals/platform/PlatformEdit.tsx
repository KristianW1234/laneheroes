import { useState } from 'react';
import { Platform } from '@/types/platform';
import { createInputChangeHandler } from '@/utils/handleInputChange';
import validatePlatformForm from '@/utils/validateForm/validatePlatform';

export default function PlatformEdit({
  platform,
  onClose,
  onSubmit,
  onSuccess,
}: {
  platform: Platform;
  onClose: () => void;
  onSubmit: (platformData: { id: number; platformName: string }) => Promise<void>;
  onSuccess?: () => void; 
  
}) {

  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const [form, setForm] = useState({
    platformName: platform.platformName || '',
  
  });

  
  const handleChange = createInputChangeHandler(setForm);

  const handleSubmit = async () => {
    const validationErrors = validatePlatformForm(form);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }
    const platformData = {
      id: platform.id,  
      platformName: form.platformName,
    };

    try {
      await onSubmit(platformData);  // Directly pass JSON object
      onClose();      
      onSuccess?.();
    } catch (err) {
      console.error('Failed to submit platform:', err);
    }
  };

   

  return (
    <div className="bg-green-50 w-full shadow-md rounded-lg p-6 max-w-lg md:max-w-4xl">
      <h2 className="text-2xl font-bold mb-4">Edit Platform</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

        <input
          type="text"
          name="platformName"
          placeholder="Platform Name"
          value={form.platformName}
          onChange={handleChange}
          className={`input-text ${errors.platformName ? 'border-red-500' : ''}`}
          required
        />

        
        
      </div>

      {Object.values(errors).length > 0 && (
        <div className="mt-4 bg-red-100 border border-red-400 text-red-700 text-sm rounded p-3">
          {Object.values(errors).map((msg, idx) => (
            <div key={idx}>• {msg}</div>
          ))}
        </div>
      )}

      <div className="mt-6 flex justify-end gap-4">
        <button
          onClick={onClose}
          className="btn-base btn-gray"
        >
          Cancel
        </button>
        <button
          onClick={handleSubmit}
          className="btn-base btn-blue"
        >
          Submit
        </button>
      </div>
    </div>
  );
}