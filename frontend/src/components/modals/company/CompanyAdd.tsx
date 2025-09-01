import { useEffect, useState } from 'react';
import { createInputChangeHandler } from '@/utils/handleInputChange';
import validateCompanyForm from '@/utils/validateForm/validateCompany';
import Image from 'next/image';


export default function CompanyAdd({
  onClose,
  onSubmit,
  onSuccess,
  
}: {
  onClose: () => void;
  onSubmit: (subject: string, formData : FormData) => Promise<void>;
  onSuccess?: () => void; 
}) {

  const initialFormData = {
    companyName: '',
    imgIcon: null as File | null,
  };
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [addAnother, setAddAnother] = useState(false);

  const [form, setForm] = useState(initialFormData);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
    
  const handleChange = createInputChangeHandler(setForm, setImagePreview);

  const handleSubmit = async () => {
    const validationErrors = validateCompanyForm(form);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const formData = new FormData();

    // Prepare the game JSON object
    const companyData = {
        companyName: form.companyName,
        
    };

    // Append as JSON string
    formData.append("company", new Blob([JSON.stringify(companyData)], { type: "application/json" }));

    // Append the image file (if any)
    if (form.imgIcon) {
        formData.append('imgFile', form.imgIcon);
    }

    try {
        await onSubmit("Company", formData);
        setErrors({});
        if (addAnother) {
          setForm(initialFormData);
          setImagePreview(null);
        } else {
          onClose();      // Close modal
        }
        onSuccess?.();
        
    } catch (err) {
        console.error('Failed to submit company:', err);
    }

    
    };  

    useEffect(() => {
      return () => {
        if (imagePreview) {
          URL.revokeObjectURL(imagePreview);
        }
      };
    }, [imagePreview]);

  return (
    <div className="bg-green-50 w-full shadow-md rounded-lg p-6 max-w-lg md:max-w-4xl">
      <h2 className="text-2xl font-bold mb-4">Add New Company</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

        <input
          type="text"
          name="companyName"
          placeholder="Company Name"
          value={form.companyName}
          onChange={handleChange}
          className={`input-text ${errors.companyName ? 'border-red-500' : ''}`}
          required
        />
        
      </div>


      <div className="mt-4">
        <input
          type="file"
          accept="image/*"
          onChange={handleChange}
          name="imgIcon"
          className="file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:bg-blue-600 file:text-white hover:file:bg-blue-700"
        />
      </div>

      {imagePreview && (
        <div className="mt-4">
          <p className="text-sm text-gray-600 mb-1">Preview:</p>
          <Image
            src={imagePreview}
            alt="Image preview"
            width={90}
            height={90}
            className="object-cover rounded shadow"
          />
        </div>
      )}

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