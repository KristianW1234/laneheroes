// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default function validatePlatformForm(form: any) {
    const newErrors: { [key: string]: string } = {};

    if (!form.platformName.trim()) {
      newErrors.platformName = "Platform name is required.";
    }

    return newErrors;
}

