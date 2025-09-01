import { Callsign } from '@/types/callsign';

export default function CallsignDetail({
  callsign,
  onClose,
}: {
  callsign: Callsign;
  onClose: () => void;
}) {
  
  return (
    <div className="bg-green-50 w-full shadow-md rounded-lg p-6 max-w-lg md:max-w-4xl">
      {/* Top Section: Name + Title */}
      <div className="text-center mb-4">
        <h2 className="text-2xl font-bold">{callsign.callsign}</h2>
      </div>

      
      

      {/* Details */}
      <div className="space-y-2 text-sm">
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">ID (admin only):</span>
          <span className="text-gray-900">{callsign.id}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Name:</span>
          <span className="text-gray-900">{callsign.callsign}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Plural form:</span>
          <span className="text-gray-900">{callsign.callsignPlural}</span>
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