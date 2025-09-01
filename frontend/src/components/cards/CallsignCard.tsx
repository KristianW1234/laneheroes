
import { Callsign } from '@/types/callsign';


import { CardActionHandlers } from '@/types/actionhandlers';

interface CallsignCardProps extends CardActionHandlers<Callsign> {
  callsign: Callsign;
}

export default function CallsignCard({ callsign, onEdit, onDelete, onDetail }: CallsignCardProps) {
  return (
    <div className="relative bg-white border rounded-2xl shadow p-4 flex flex-col justify-between h-auto min-h-[260px]">
      {/* Top Section */}
      <div className="flex justify-between items-start mb-2">
        <div className="grid grid-cols-[auto_1fr_auto] items-center gap-2 mb-2 w-full">
          <div className="text-center">
            <div className="flex flex-col items-center leading-tight">
              <span className="font-bold text-lg">
                {callsign.callsign}/{callsign.callsignPlural}
              </span>
              
            </div>
          </div>

          
        </div>
      </div>

      {/* Buttons */}
      <div className="flex justify-between mt-auto">
        <button
          className="btn-base btn-blue"
          onClick={() => onDetail(callsign)}
        >
          Detail
        </button>
        <button
          className="btn-base btn-gray"
          onClick={() => onEdit(callsign)}
        >
          Edit
        </button>
        <button
          className="btn-base btn-red"
          onClick={() => onDelete(callsign)}
        >
          Delete
        </button>
      </div>
    </div>
  );
}