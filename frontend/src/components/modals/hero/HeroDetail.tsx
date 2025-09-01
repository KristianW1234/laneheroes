import { Hero } from '@/types/hero';
import { baseURL } from '@/utils/constants';
import { testImageExists } from "@/utils/testImageExists";
import { useEffect, useState } from 'react';
import Image from 'next/image';

export default function HeroDetail({
  hero,
  onClose,
}: {
  hero: Hero;
  onClose: () => void;
}) {

  const [heroImgSrc, setHeroImgSrc] = useState<string | null>(`${baseURL}/images/hero/${hero.imgIcon}`);
  useEffect(() => {
      testImageExists(`${heroImgSrc}`, () => setHeroImgSrc(`${baseURL}/images/noimage.png`));
    }, [heroImgSrc]);
  
  return (
    <div className="bg-green-50 w-full shadow-md rounded-lg p-6 max-w-lg md:max-w-4xl">
      {/* Top Section: Name + Title */}
      <div className="text-center mb-4">
        <h2 className="text-2xl font-bold">{hero.heroName}</h2>
        <p className="text-gray-600 italic">{hero.heroTitle}</p>
      </div>

      {/* Image */}
      <div className="flex justify-center mb-6">
        <Image
          src={`${heroImgSrc}`}
          alt={hero.heroName}
          width={90}
          height={90}
          className="object-cover rounded border"
        />
      </div>

      {/* Details */}
      <div className="space-y-2 text-sm">
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">ID (admin only):</span>
          <span className="text-gray-900">{hero.id}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Alternate Name:</span>
          <span className="text-gray-900">{hero.alternateName || '-'}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Gender:</span>
          <span className="text-gray-900">{hero.heroGender}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Game:</span>
          <span className="text-gray-900">{hero.game?.gameName}</span>
        </div>
      </div>

      {/* Description */}
      <div className="mt-6">
        <h3 className="font-semibold text-gray-800 mb-1">Description:</h3>
        <p className="text-gray-700 text-sm whitespace-pre-line">
          {hero.heroDescription || 'No description available.'}
        </p>
      </div>

      {/* Lore (assuming it's appended to description or split out later) */}
      <div className="mt-4">
        <h3 className="font-semibold text-gray-800 mb-1">Lore:</h3>
        <p className="text-gray-700 text-sm whitespace-pre-line">
          
          {hero.heroLore || 'No lore available.'}
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