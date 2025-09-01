import { useEffect, useState } from 'react';
import { Game } from '@/types/game';
import { createInputChangeHandler } from '@/utils/handleInputChange';
import validateHeroForm from '@/utils/validateForm/validateHero';
import Image from 'next/image';

export default function HeroAdd({
  onClose,
  onSubmit,
  onSuccess,
  games,
}: {
  onClose: () => void;
  onSubmit: (subject: string, formData : FormData) => Promise<void>;
  onSuccess?: () => void; 
  games: Game[];
}) {

  const initialFormData ={
    heroName: '',
    heroGender: '',
    gameId: '',
    heroTitle: '',
    alternateName: '',
    displayByTitle: false,
    heroDescription: '',
    heroLore: '',
    imgIcon: null as File | null,
  }

  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [addAnother, setAddAnother] = useState(false);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
  const [form, setForm] = useState(initialFormData);

  const handleChange = createInputChangeHandler(setForm, setImagePreview);

  const handleSubmit = async () => {
    const validationErrors = validateHeroForm(form);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }
    const formData = new FormData();

    // Prepare the hero JSON object
    const heroData = {
        heroName: form.heroName,
        heroGender: form.heroGender,
        heroTitle: form.heroTitle,
        alternateName: form.alternateName,
        displayByTitle: form.displayByTitle,
        heroDescription: form.heroDescription,
        heroLore: form.heroLore,
        game: {
        id: Number(form.gameId), // Ensure ID is numeric
        },
    };

    // Append as JSON string
    formData.append("hero", new Blob([JSON.stringify(heroData)], { type: "application/json" }));

    // Append the image file (if any)
    if (form.imgIcon) {
        formData.append('imgFile', form.imgIcon);
    }

    
    try {
        await onSubmit("Hero", formData);
        setErrors({});
        if (addAnother){
          setForm(initialFormData);
          setImagePreview(null);
        } else {
          onClose();      // Close modal
        }
        
        onSuccess?.();
        
    } catch (err) {
        console.error('Failed to submit hero:', err);
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
      <h2 className="text-2xl font-bold mb-4">Add New Hero</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

        <input
          type="text"
          name="heroName"
          placeholder="Hero Name"
          value={form.heroName}
          onChange={handleChange}
          className={`input-text ${errors.heroName ? 'border-red-500' : ''}`}
          required
        />

        <select
          name="heroGender"
          value={form.heroGender}
          onChange={handleChange}
          className={`input-text ${errors.heroGender ? 'border-red-500' : ''}`}
          required
        >
          <option value="">Select Gender</option>
          <option value="MALE">Male</option>
          <option value="FEMALE">Female</option>
          <option value="UNKNOWN">Unknown</option>
        </select>

        <select
          name="gameId"
          value={form.gameId}
          onChange={handleChange}
          className={`input-text ${errors.gameId ? 'border-red-500' : ''}`}
          required
        >
          <option value="">Select Game</option>
          {games.map((g) => (
            <option key={g.id} value={g.id}>{g.gameName}</option>
          ))}
        </select>

        <input
          type="text"
          name="heroTitle"
          placeholder="Hero Title (optional)"
          value={form.heroTitle}
          onChange={handleChange}
          className="input-text"
        />

        <input
          type="text"
          name="alternateName"
          placeholder="Alternate Name (optional)"
          value={form.alternateName}
          onChange={handleChange}
          className="input-text"
        />

        <label className="flex items-center gap-2 mt-2">
          <input
            type="checkbox"
            name="displayByTitle"
            checked={form.displayByTitle}
            onChange={handleChange}
          />
          Display by Title
        </label>
      </div>

      <textarea
        name="heroDescription"
        placeholder="Description (optional)"
        value={form.heroDescription}
        onChange={handleChange}
        className="input-text mt-4 w-full"
        rows={3}
      />

      <textarea
        name="heroLore"
        placeholder="Lore (optional)"
        value={form.heroLore}
        onChange={handleChange}
        className="input-text mt-2 w-full"
        rows={4}
      />

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