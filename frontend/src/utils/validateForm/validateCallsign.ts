// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default function validateCallsignForm(form: any) {
    const newErrors: { [key: string]: string } = {};

    if (!form.callsign.trim()) {
      newErrors.callsign = "Callsign is required.";
    }

    if (!form.callsignPlural.trim()) {
      newErrors.callsignPlural = "Callsign Plural is required.";
    }

    return newErrors;
}

