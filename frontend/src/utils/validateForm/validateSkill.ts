// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default function validateSkillForm(form: any) {
    const newErrors: { [key: string]: string } = {};

    if (!form.skillName.trim()) {
      newErrors.skillName = "Skill name is required.";
    }

    if (!form.skillSlot) {
      newErrors.skillSlot = "Skill slot is required.";
    }

    if (!form.heroId) {
      newErrors.heroId = "Hero is required.";
    }

    if (!form.skillTypes || form.skillTypes.length === 0) {
      newErrors.skillTypes = "At least one skill type must be selected.";
    }

    

    return newErrors;
}

