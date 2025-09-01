import { Game } from '@/types/game';
import { baseURL } from '@/utils/constants';
import { testImageExists } from '@/utils/testImageExists';
import { useEffect, useState } from 'react';
import Image from 'next/image';

export default function GameDetail({
  game,
  onClose,
}: {
  game: Game;
  onClose: () => void;
}) {

  const [gameImgSrc, setGameImgSrc] = useState<string | null>(`${baseURL}/images/game/${game.imgIcon}`);
    useEffect(() => {
        testImageExists(`${gameImgSrc}`, () => setGameImgSrc(`${baseURL}/images/noimage.png`));
      }, [gameImgSrc]);
  
  return (
    <div className="bg-green-50 w-full shadow-md rounded-lg p-6 max-w-lg md:max-w-4xl">
      {/* Top Section: Name + Title */}
      <div className="text-center mb-4">
        <h2 className="text-2xl font-bold">{game.gameName}</h2>
      </div>

      {/* Image */}
      <div className="flex justify-center mb-6">
        <Image
          src={`${gameImgSrc}`}
          alt={game.gameName}
          width={90}
          height={90}
          className="object-cover rounded shadow"
        />
      </div>

      {/* Details */}
      <div className="space-y-2 text-sm">
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">ID (admin only):</span>
          <span className="text-gray-900">{game.id}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Name:</span>
          <span className="text-gray-900">{game.gameName}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Code:</span>
          <span className="text-gray-900">{game.gameCode}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Callsign:</span>
          <span className="text-gray-900">{game.callsign?.callsign} / {game.callsign?.callsignPlural}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Platform:</span>
          <span className="text-gray-900">{game.platform?.platformName}</span>
        </div>

        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Company:</span>
          <span className="text-gray-900">{game.company?.companyName}</span>
        </div>

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