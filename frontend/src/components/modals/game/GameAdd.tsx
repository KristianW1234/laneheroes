import { useEffect, useState } from 'react';
import { Platform } from '@/types/platform';
import { Callsign } from '@/types/callsign';
import { Company } from '@/types/company';
import { createInputChangeHandler } from '@/utils/handleInputChange';
import validateGameForm from '@/utils/validateForm/validateGame';
import Image from 'next/image';


export default function GameAdd({
  onClose,
  onSubmit,
  onSuccess,
  platforms,
  callsigns, 
  companies
}: {
  onClose: () => void;
  onSubmit: (subject: string, formData : FormData) => Promise<void>;
  onSuccess?: () => void; 
  platforms: Platform[];
  callsigns: Callsign[];
  companies: Company[];
}) {

  const initialFormData = {
    gameName: '',
    gameCode: '',
    platformId: '',
    callsignId: '',
    companyId: '',
    imgIcon: null as File | null,
  };

  const [imagePreview, setImagePreview] = useState<string | null>(null);

  const [form, setForm] = useState(initialFormData);
  const [addAnother, setAddAnother] = useState(false);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const handleChange = createInputChangeHandler(setForm, setImagePreview);

  const handleSubmit = async () => {
    const validationErrors = validateGameForm;
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const formData = new FormData();

    // Prepare the game JSON object
    const gameData = {
        gameName: form.gameName,
        gameCode: form.gameCode,
        platform: {
            id: Number(form.platformId),
        },
        callsign: {
            id: Number(form.callsignId),
        },
        company: {
            id: Number(form.companyId),
        },
    };

    // Append as JSON string
    formData.append("game", new Blob([JSON.stringify(gameData)], { type: "application/json" }));

    // Append the image file (if any)
    if (form.imgIcon) {
        formData.append('imgFile', form.imgIcon);
    }

    try {
        await onSubmit("Game",formData);
        setErrors({});
        if (addAnother) {
          setForm(initialFormData);
          setImagePreview(null);
        } else {
          onClose();      // Close modal
        }
        onSuccess?.();
        
    } catch (err) {
        console.error('Failed to submit game:', err);
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
      <h2 className="text-2xl font-bold mb-4">Add New Game</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

        <input
          type="text"
          name="gameName"
          placeholder="Game Name"
          value={form.gameName}
          onChange={handleChange}
          className={`input-text ${errors.gameName ? 'border-red-500' : ''}`}
          required
        />

        <input
          type="text"
          name="gameCode"
          placeholder="Game Code"
          value={form.gameCode}
          onChange={handleChange}
          className={`input-text ${errors.gameCode ? 'border-red-500' : ''}`}
          required
        />

        <select
          name="platformId"
          value={form.platformId}
          onChange={handleChange}
          className={`input-text ${errors.platformId ? 'border-red-500' : ''}`}
          required
        >
          <option value="">Select Platform</option>
          {platforms.map((p) => (
            <option key={p.id} value={p.id}>{p.platformName}</option>
          ))}
        </select>

        <select
          name="callsignId"
          value={form.callsignId}
          onChange={handleChange}
          className={`input-text ${errors.callsignId ? 'border-red-500' : ''}`}
          required
        >
          <option value="">Select Callsign</option>
          {callsigns.map((c) => (
            <option key={c.id} value={c.id}>{c.callsign} / {c.callsignPlural}</option>
          ))}
        </select>

        <select
          name="companyId"
          value={form.companyId}
          onChange={handleChange}
          className={`input-text ${errors.companyId ? 'border-red-500' : ''}`}
          required
        >
          <option value="">Select Company</option>
          {companies.map((c) => (
            <option key={c.id} value={c.id}>{c.companyName}</option>
          ))}
        </select>

        
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