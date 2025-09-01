import Image from 'next/image';

import { Hero } from '@/types/hero';

import { baseURL } from '@/utils/constants';

import { CardActionHandlers } from '@/types/actionhandlers';
import { useEffect, useState } from 'react';

import { testImageExists } from "@/utils/testImageExists";

interface HeroCardProps extends CardActionHandlers<Hero> {
  hero: Hero;
}

export default function HeroCard({ hero, onEdit, onDelete, onDetail }: HeroCardProps) {

  
  const [heroImgSrc, setHeroImgSrc] = useState<string | null>(`${baseURL}/images/hero/${hero.imgIcon}`);
  const [gameImgSrc, setGameImgSrc] = useState<string | null>(`${baseURL}/images/game/${hero.game.imgIcon}`);

  const [skillImgSrcMap, setSkillImgSrcMap] = useState<Record<number, string>>({});

  const heroCodeParts = hero.heroCode.split('_');
      const heroFolder = heroCodeParts[0].toLowerCase();
      const gameFolder = heroCodeParts[1].toLowerCase();


useEffect(() => {
    testImageExists(`${heroImgSrc}`, () => setHeroImgSrc(`${baseURL}/images/noimage.png`));
    testImageExists(`${gameImgSrc}`, () => setGameImgSrc(`${baseURL}/images/noimage.png`));
    
  }, [heroImgSrc, gameImgSrc]);

  useEffect(() => {
  
    hero.skills.forEach(async (skill) => {
      

      let testImgSrc = `${baseURL}/images/skill/${gameFolder}/${heroFolder}/${skill.imgIcon}`;

      if (skill.imgIcon === "dota-innate.png") {
        testImgSrc = `${baseURL}/images/skill/${gameFolder}/${skill.imgIcon}`;
      }

      await testImageExists(testImgSrc, () => {
        // On fail: set fallback
        setSkillImgSrcMap(prev => ({
          ...prev,
          [skill.id]: `${baseURL}/images/noimage.png`
        }));
      });

      // If no fail was triggered, assume it's valid
      setSkillImgSrcMap(prev => ({
        ...prev,
        [skill.id]: testImgSrc
      }));
    });
  }, [hero.skills, gameImgSrc, heroImgSrc, gameFolder, heroFolder]);



  return (
    <div className="relative bg-white border rounded-2xl shadow p-4 flex flex-col justify-between h-auto min-h-[260px]">
      {/* Top Section */}
      <div className="flex justify-between items-start mb-2">
        {/* Game Name and Hero Info */}
        <div className="grid grid-cols-[auto_1fr_auto] items-center gap-2 mb-2 w-full">
          {/* Game Icon (Left) */}
          <div className="w-[90px] h-[90px] justify-self-start">
            <Image
              src={`${gameImgSrc}`}
              width={90}
              height={90}
              alt={`${hero.game.gameName} icon`}
              className="rounded object-contain"
            />
          </div>

          
          {/* Hero Info (Center) */}
          <div className="text-center">
            <div className="flex flex-col items-center leading-tight">
              <span className={hero.displayByTitle === "Y" ? "italic text-sm text-gray-600" : "font-bold text-lg"}>
                {hero.heroName}
              </span>
              <span className={hero.displayByTitle === "Y" ? "font-bold text-lg" : "italic text-sm text-gray-600"}>
                {hero.heroTitle}
              </span>
            </div>
          </div>

          {/* Hero Portrait (Right) */}
          <div className="w-[90px] h-[90px] rounded overflow-hidden justify-self-end">
            <Image
              src={`${heroImgSrc}`}
              width={90}
              height={90}
              alt={`${hero.heroName} portrait`}
            />
          </div>
          
        </div>
      </div>

      {/* Description Section */}
      <div className="text-gray-800 line-clamp-3 mb-5">
        {hero.heroDescription}
      </div>

     {/* Skill Icons Section */}
      <div className="flex items-center justify-center gap-2 mt-2 mb-2">
        {hero.skills.map(skill => (
          <div key={skill.id} className="w-[30px] h-[30px] flex items-center justify-center">
            <Image
              src={skillImgSrcMap[skill.id] || '/loading-placeholder.png'}
              alt={skill.skillName}
              width={30}
              height={30}
              className="rounded shadow"
            />
          </div>
        ))}
      </div>

      {/* Buttons */}
      <div className="flex justify-between mt-2">
        <button
          className="btn-base btn-blue"
          onClick={() => onDetail(hero)}
        >
          Detail
        </button>
        <button
          className="btn-base btn-gray"
          onClick={() => onEdit(hero)}
        >
          Edit
        </button>
        <button
          className="btn-base btn-red"
          onClick={() => onDelete(hero)}
        >
          Delete
        </button>
      </div>
    </div>
  );
}