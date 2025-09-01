export function createInputChangeHandler<T extends object>(
  setForm: React.Dispatch<React.SetStateAction<T>>,
  setImagePreview?: (url: string | null) => void
) {
  return (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const { name, value, type, checked, files } = e.target as any;

    if (type === 'checkbox') {
      setForm(prev => ({ ...prev, [name]: checked }));
    } else if (type === 'file') {
      const file = files?.[0];
      setForm(prev => ({ ...prev, [name]: file }));
      if (setImagePreview) {
        setImagePreview(file ? URL.createObjectURL(file) : null);
      }
    } else {
      setForm(prev => ({ ...prev, [name]: value }));
    }
  };
}
