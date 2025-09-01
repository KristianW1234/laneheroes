// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default function validateHeroForm(form: any) {
    const newErrors: { [key: string]: string } = {};

    if (!form.heroName.trim()) {
      newErrors.heroName = "Hero name is required.";
    }

    if (!form.gameId) {
      newErrors.gameId = "Game is required.";
    }

    if (!form.heroGender) {
      newErrors.heroGender = "Hero gender is required.";
    }

    return newErrors;
}

