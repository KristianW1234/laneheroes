import { useState } from 'react';
import { createInputChangeHandler } from '@/utils/handleInputChange';
import validateCallsignForm from '@/utils/validateForm/validateCallsign';


export default function CallsignAdd({
  onClose,
  onSubmit,
  onSuccess,
}: {
  onClose: () => void;
  onSubmit: (subject: string, callsignData: { callsign: string, callsignPlural: string }) => Promise<void>;
  onSuccess?: () => void;

}) {
  const initialFormData = {
    callsign: '',
    callsignPlural: '',
  };
  
  const [form, setForm] = useState(initialFormData);
  const [addAnother, setAddAnother] = useState(false);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
  
  const handleChange = createInputChangeHandler(setForm);

  const handleSubmit = async () => {
    const validationErrors = validateCallsignForm(form);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }



    const callsignData = {
      callsign: form.callsign,
      callsignPlural: form.callsignPlural,
    };

    try {
      await onSubmit("Callsign", callsignData);  // Directly pass JSON object
      setErrors({});
      if (addAnother) {
          setForm(initialFormData);
          
        } else {
          onClose();      // Close modal
        }
      onSuccess?.();
    } catch (err) {
      console.error('Failed to submit callsign:', err);
    }
  };

   

  return (
    <div className="bg-green-50 w-full shadow-md rounded-lg p-6 max-w-lg md:max-w-4xl">
      <h2 className="text-2xl font-bold mb-4">Add New Callsign</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

        <input
          type="text"
          name="callsign"
          placeholder="Callsign Name"
          value={form.callsign}
          onChange={handleChange}
          className={`input-text ${errors.callsign ? 'border-red-500' : ''}`}
          required
        />

        <input
          type="text"
          name="callsignPlural"
          placeholder="Plural Form"
          value={form.callsignPlural}
          onChange={handleChange}
          className={`input-text ${errors.callsignPlural ? 'border-red-500' : ''}`}
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
        <label className="flex items-center gap-2 mt-4">
        <input
          type="checkbox"
          checked={addAnother}
          onChange={() => setAddAnother(prev => !prev)}
        />
        <span>Keep modal open to add another</span>
      </label>
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