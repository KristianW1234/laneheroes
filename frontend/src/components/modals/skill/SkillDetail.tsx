import { Skill } from '@/types/skill';
import { baseURL } from '@/utils/constants';
import { testImageExists } from "@/utils/testImageExists";
import { useEffect, useState } from 'react';
import Image from 'next/image';

export default function SkillDetail({
  skill,
  onClose,
}: {
  skill: Skill;
  onClose: () => void;
}) {

  const heroCodeParts = skill.heroCode.split('_');
  const heroFolder = heroCodeParts[0].toLowerCase();
  const gameFolder = heroCodeParts[1].toLowerCase();

  let testImgSrc = `${baseURL}/images/skill/${gameFolder}/${heroFolder}/${skill.imgIcon}`;

  if (skill.imgIcon === "dota-innate.png"){
    testImgSrc = `${baseURL}/images/skill/${gameFolder}/${skill.imgIcon}`;
  }

  const [skillImgSrc, setSkillImgSrc] = useState<string | null>(testImgSrc);
  const [heroImgSrc, setHeroImgSrc] = useState<string | null>(`${baseURL}/images/hero/${skill.heroImgIcon}`);

  useEffect(() => {
      testImageExists(`${skillImgSrc}`, () => setSkillImgSrc(`${baseURL}/images/noimage.png`));
      testImageExists(`${heroImgSrc}`, () => setHeroImgSrc(`${baseURL}/images/noimage.png`));
    }, [skillImgSrc,heroImgSrc]);
  
  return (
    <div className="bg-green-50 w-full shadow-md rounded-lg p-6 max-w-lg md:max-w-4xl">
      {/* Top Section: Name + Title */}
      <div className="text-center mb-4">
        <h2 className="text-2xl font-bold">{skill.skillName}</h2>
      </div>

      {/* Image */}
      <div className="flex justify-center mb-6">
        <Image
          src={`${skillImgSrc}`}
          alt={skill.skillName}
          width={90}
          height={90}
          className="object-cover rounded border"
        />
      </div>

      {/* Details */}
      <div className="space-y-2 text-sm">
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">ID (admin only):</span>
          <span className="text-gray-900">{skill.id}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Slot:</span>
          <span className="text-gray-900">{skill.skillSlot || '-'}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Type:</span>
          <span className="text-gray-900">{skill.skillTypes}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Passive:</span>
          <span className="text-gray-900">{skill.isPassive === 'Y' ? 'Yes' : 'No'}</span>
        </div>

        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Ultimate:</span>
          <span className="text-gray-900">{skill.isUltimate === 'Y' ? 'Yes' : 'No'}</span>
        </div>

        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Owner:</span>
          <Image
            src={`${heroImgSrc}`}
            alt={skill.heroCode}
            width={90}
            height={90}
            className="object-cover rounded border"
          />
        </div>
      </div>

      {/* Description */}
      <div className="mt-6">
        <h3 className="font-semibold text-gray-800 mb-1">Description:</h3>
        <p className="text-gray-700 text-sm whitespace-pre-line">
          {skill.skillDescription || 'No description available.'}
        </p>
      </div>

      {/* Close Button */}
      <div className="flex justify-end mt-6">
        <button
          onClick={onClose}
          className="btn-base btn-blue"
        >
          Close
        </button>
      </div>
    </div>
  );
}