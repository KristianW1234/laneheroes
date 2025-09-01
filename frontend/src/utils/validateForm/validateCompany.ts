// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default function validateCompanyForm(form: any) {
    const newErrors: { [key: string]: string } = {};

    if (!form.companyName.trim()) {
      newErrors.companyName = "Company name is required.";
    }

    return newErrors;
}

