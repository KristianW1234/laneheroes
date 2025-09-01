import Image from 'next/image';

import { Game } from '@/types/game';

import { baseURL } from '@/utils/constants';

import { CardActionHandlers } from '@/types/actionhandlers';

import { testImageExists } from "@/utils/testImageExists";
import { useEffect, useState } from 'react';

interface GameCardProps extends CardActionHandlers<Game> {
  game: Game;
}

export default function GameCard({ game, onEdit, onDelete, onDetail }: GameCardProps) {

  const [gameImgSrc, setGameImgSrc] = useState<string | null>(`${baseURL}/images/game/${game.imgIcon}`);
    const [companyImgSrc, setCompanyImgSrc] = useState<string | null>(`${baseURL}/images/company/${game.company.imgIcon}`);
  
  
  useEffect(() => {
      testImageExists(`${gameImgSrc}`, () => setGameImgSrc(`${baseURL}/images/noimage.png`));
      testImageExists(`${companyImgSrc}`, () => setCompanyImgSrc(`${baseURL}/images/noimage.png`));
      
    }, [gameImgSrc, companyImgSrc]);

  return (
    <div className="relative bg-white border rounded-2xl shadow p-4 flex flex-col justify-between h-auto min-h-[260px]">
      {/* Top Section */}
      <div className="flex justify-between items-start mb-2">
        
        <div className="grid grid-cols-[auto_1fr_auto] items-center gap-2 mb-2 w-full">
          
          <div className="w-[90px] h-[90px] justify-self-start">
            <Image
              src={`${companyImgSrc}`}
              width={90}
              height={90}
              alt={`${game.gameName} icon`}
              className="rounded object-contain"
            />
          </div>

          {/* Game Info (Center) */}
          <div className="text-center">
            <div className="flex flex-col items-center leading-tight">
              <span className="font-bold text-lg">
                {game.gameName}
              </span>
              
            </div>
          </div>

          {/* Game Portrait (Right) */}
          <div className="w-[90px] h-[90px] rounded overflow-hidden justify-self-end">
            <Image
              src={`${gameImgSrc}`}
              width={90}
              height={90}
              alt={`${game.gameName} portrait`}
            />
          </div>
        </div>
      </div>

      {/* Buttons */}
      <div className="flex justify-between mt-auto">
        <button
          className="btn-base btn-blue"
          onClick={() => onDetail(game)}
        >
          Detail
        </button>
        <button
          className="btn-base btn-gray"
          onClick={() => onEdit(game)}
        >
          Edit
        </button>
        <button
          className="btn-base btn-red"
          onClick={() => onDelete(game)}
        >
          Delete
        </button>
      </div>
    </div>
  );
}