import { Platform } from '@/types/platform';

import { CardActionHandlers } from '@/types/actionhandlers';

interface PlatformCardProps extends CardActionHandlers<Platform> {
  platform: Platform;
}

export default function PlatformCard({ platform, onEdit, onDelete, onDetail }: PlatformCardProps) {
  return (
    <div className="relative bg-white border rounded-2xl shadow p-4 flex flex-col justify-between h-auto min-h-[260px]">
      {/* Top Section */}
      <div className="flex justify-between items-start mb-2">
        <div className="grid grid-cols-[auto_1fr_auto] items-center gap-2 mb-2 w-full">
          <div className="text-center">
            <div className="flex flex-col items-center leading-tight">
              <span className="font-bold text-lg">
                {platform.platformName}
              </span>
              
            </div>
          </div>

          
        </div>
      </div>

      {/* Buttons */}
      <div className="flex justify-between mt-auto">
        <button
          className="btn-base btn-blue"
          onClick={() => onDetail(platform)}
        >
          Detail
        </button>
        <button
          className="btn-base btn-gray"
          onClick={() => onEdit(platform)}
        >
          Edit
        </button>
        <button
          className="btn-base btn-red"
          onClick={() => onDelete(platform)}
        >
          Delete
        </button>
      </div>
    </div>
  );
}