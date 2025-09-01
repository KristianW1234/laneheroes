import Image from 'next/image';

import { Company } from '@/types/company';

import { baseURL } from '@/utils/constants';

import { CardActionHandlers } from '@/types/actionhandlers';

import { testImageExists } from "@/utils/testImageExists";
import { useEffect, useState } from 'react';

interface CompanyCardProps extends CardActionHandlers<Company> {
  company: Company;
}

export default function CompanyCard({ company, onEdit, onDelete, onDetail }: CompanyCardProps) {

  const [companyImgSrc, setCompanyImgSrc] = useState<string | null>(`${baseURL}/images/company/${company.imgIcon}`);

  useEffect(() => {
        testImageExists(`${companyImgSrc}`, () => setCompanyImgSrc(`${baseURL}/images/noimage.png`));
        
      }, [companyImgSrc]);

  return (
    <div className="relative bg-white border rounded-2xl shadow p-4 flex flex-col justify-between h-auto min-h-[260px]">
      {/* Top Section */}
        <div className="flex justify-between items-start mb-2">
          <div className="w-full flex justify-center mb-2">
            <div className="w-[90px] h-[90px]">
              <Image
                src={`${companyImgSrc}`}
                width={90}
                height={90}
                alt={`${company.companyName} logo`}
              />
            </div>
          </div>
        </div>

      {/* Description Section */}
      <div className="text-gray-800 line-clamp-3 mb-5 text-center">
        {company.companyName}
      </div>

      {/* Buttons */}
      <div className="flex justify-between mt-auto">
        <button
          className="btn-base btn-blue"
          onClick={() => onDetail(company)}
        >
          Detail
        </button>
        <button
          className="btn-base btn-gray"
          onClick={() => onEdit(company)}
        >
          Edit
        </button>
        <button
          className="btn-base btn-red"
          onClick={() => onDelete(company)}
        >
          Delete
        </button>
      </div>
    </div>
  );
}