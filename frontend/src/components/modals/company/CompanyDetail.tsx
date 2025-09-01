import { Company } from '@/types/company';
import { baseURL } from '@/utils/constants';
import { testImageExists } from '@/utils/testImageExists';
import { useEffect, useState } from 'react';
import Image from 'next/image';

export default function PlatformDetail({
  company,
  onClose,
}: {
  company: Company;
  onClose: () => void;
}) {
  const [companyImgSrc, setCompanyImgSrc] = useState<string | null>(`${baseURL}/images/company/${company.imgIcon}`);
      useEffect(() => {
          testImageExists(`${companyImgSrc}`, () => setCompanyImgSrc(`${baseURL}/images/noimage.png`));
        }, [companyImgSrc]);
  return (
    <div className="bg-green-50 w-full shadow-md rounded-lg p-6 max-w-lg md:max-w-4xl">
      {/* Top Section: Name + Title */}
      <div className="text-center mb-4">
        <h2 className="text-2xl font-bold">{company.companyName}</h2>
      </div>

      {/* Image */}
      <div className="flex justify-center mb-6">
        <Image
          src={`${companyImgSrc}`}
          alt={company.companyName}
          className="w-full max-w-[90px] h-auto rounded object-cover border"
          width={90}
          height={90}
        />
      </div>
      

      {/* Details */}
      <div className="space-y-2 text-sm">
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">ID (admin only):</span>
          <span className="text-gray-900">{company.id}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Name:</span>
          <span className="text-gray-900">{company.companyName}</span>
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