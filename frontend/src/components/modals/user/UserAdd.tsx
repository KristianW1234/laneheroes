import { useState } from 'react';
import { createInputChangeHandler } from '@/utils/handleInputChange';
import { isValidEmail } from '@/utils/isValidEmail';



export default function UserAdd({
  onClose,
  onSubmit,
  onSuccess,
}: {
  onClose: () => void;
  onSubmit: (subject: string, userData: { userName: string, userPassword: string, userRole: string, userEmail: string, isActive: boolean }) => Promise<void>;
  onSuccess?: () => void; 
  
}) {
  const initialFormData = {
    userName: '',
    userPassword: '',
    userRole: '',
    userEmail: '',
    isActive: false
  };

  
  const [form, setForm] = useState(initialFormData);
  const [addAnother, setAddAnother] = useState(false);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const validateForm = () => {
    const newErrors: { [key: string]: string } = {};

    if (!form.userName.trim()) {
      newErrors.userName = "User name is required.";
    }

    if (!form.userPassword.trim()) {
      newErrors.userPassword = "User password is required.";
    }

    if (!form.userRole) {
      newErrors.userRole = "User role is required.";
    }

    if (!isValidEmail(form.userEmail)) {
      newErrors.userEmail = "Invalid email format.";
    }

   return newErrors;
  };
  const handleChange = createInputChangeHandler(setForm);

  const handleSubmit = async () => {
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const userData = {
      userName: form.userName,
      userPassword: form.userPassword,
      userRole: form.userRole,
      userEmail: form.userEmail,
      isActive: form.isActive
    };

    try {
      await onSubmit("User", userData);  // Directly pass JSON object
      setErrors({});
      if (addAnother) {
          setForm(initialFormData);
          
        } else {
          onClose();      // Close modal
        }      
      onSuccess?.();
    } catch (err) {
      console.error('Failed to submit user:', err);
    }
  };

   

  return (
    <div className="bg-green-50 w-full shadow-md rounded-lg p-6 max-w-lg md:max-w-4xl">
      <h2 className="text-2xl font-bold mb-4">Add New User</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

        <input
          type="text"
          name="userName"
          placeholder="User Name"
          value={form.userName}
          onChange={handleChange}
          className={`input-text ${errors.userName ? 'border-red-500' : ''}`}
          required
        />

        <input
          type="password"
          name="userPassword"
          placeholder="User Password"
          value={form.userPassword}
          onChange={handleChange}
          className={`input-text ${errors.userPassword ? 'border-red-500' : ''}`}
          required
        />

        <input
          type="text"
          name="userEmail"
          placeholder="User Email"
          value={form.userEmail}
          onChange={handleChange}
          className={`input-text ${errors.userEmail ? 'border-red-500' : ''}`}
          required
        />

        <select
          name="userRole"
          value={form.userRole}
          onChange={handleChange}
          className={`input-text ${errors.userRole ? 'border-red-500' : ''}`}
          required
        >
          <option value="">Select Role</option>
          <option value="ADMIN">Admin</option>
          <option value="MODERATOR">Moderator</option>
          <option value="USER">User</option>
        </select>

        <label className="flex items-center gap-2 mt-2">
          <input
            type="checkbox"
            name="isActive"
            checked={form.isActive}
            onChange={handleChange}
          />
          Is Active?
        </label>

        
        
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