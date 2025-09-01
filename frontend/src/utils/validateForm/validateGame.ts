// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default function validateGameForm(form: any) {
    const newErrors: { [key: string]: string } = {};

    if (!form.gameName.trim()) {
      newErrors.gameName = "Game name is required.";
    }

    if (!form.gameCode.trim()) {
      newErrors.gameCode = "Game code is required.";
    }

    if (!form.platformId) {
      newErrors.platformId = "Platform is required.";
    }

    if (!form.callsignId) {
      newErrors.callsignId = "Callsign is required.";
    }

    if (!form.companyId) {
      newErrors.companyId = "Company is required.";
    }

    return newErrors;
}

