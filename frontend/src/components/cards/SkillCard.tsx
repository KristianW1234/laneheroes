import Image from 'next/image';

import { Skill } from '@/types/skill';

import { baseURL } from '@/utils/constants';

import { CardActionHandlers } from '@/types/actionhandlers';

import { testImageExists } from "@/utils/testImageExists";
import { useEffect, useState } from 'react';

interface SkillCardProps extends CardActionHandlers<Skill> {
  skill: Skill;
}

export default function SkillCard({ skill, onEdit, onDelete, onDetail }: SkillCardProps) {
  
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

    }, [skillImgSrc, heroImgSrc]);

  return (
    <div className="relative bg-white border rounded-2xl shadow p-4 flex flex-col justify-between h-auto min-h-[260px]">
      {/* Top Section */}
      <div className="flex justify-between items-start mb-2">
        
        <div className="grid grid-cols-[auto_1fr_auto] items-center gap-2 mb-2 w-full">
          
          <div className="w-[90px] h-[90px] justify-self-start">
            <Image
              src={`${heroImgSrc}`}
              width={90}
              height={90}
              alt={`${skill.skillName} icon`}
              className="rounded object-contain"
            />
          </div>

          {/* Skill Info (Center) */}
          <div className="text-center">
            <div className="flex flex-col items-center leading-tight">
              <span className="font-bold text-lg">
                {skill.skillName}
              </span>
              
            </div>
          </div>

          {/* Skill Portrait (Right) */}
          <div className="w-[90px] h-[90px] rounded overflow-hidden justify-self-end">
            <Image
              src={`${skillImgSrc}`}
              width={90}
              height={90}
              alt={`${skill.skillName} portrait`}
            />
          </div>
        </div>
      </div>

      {/* Description Section */}
      <div className="text-gray-800 line-clamp-3 mb-5">
        {skill.skillDescription}
      </div>

      {/* Buttons */}
      <div className="flex justify-between mt-auto">
        <button
          className="btn-base btn-blue"
          onClick={() => onDetail(skill)}
        >
          Detail
        </button>
        <button
          className="btn-base btn-gray"
          onClick={() => onEdit(skill)}
        >
          Edit
        </button>
        <button
          className="btn-base btn-red"
          onClick={() => onDelete(skill)}
        >
          Delete
        </button>
      </div>
    </div>
  );
}