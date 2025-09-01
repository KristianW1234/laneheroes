import { User } from '@/types/user';


export default function PlatformDetail({
  user,
  onClose,
}: {
  user: User;
  onClose: () => void;
}) {
  
  return (
    <div className="bg-green-50 w-full shadow-md rounded-lg p-6 max-w-lg md:max-w-4xl">
      {/* Top Section: Name + Title */}
      <div className="text-center mb-4">
        <h2 className="text-2xl font-bold">{user.userName}</h2>
      </div>

      
      

      {/* Details */}
      <div className="space-y-2 text-sm">
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">ID (admin only):</span>
          <span className="text-gray-900">{user.id}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Email:</span>
          <span className="text-gray-900">{user.userEmail}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Role:</span>
          <span className="text-gray-900">{user.userRole}</span>
        </div>
        <div className="flex justify-between">
          <span className="font-medium text-gray-700">Is Active?</span>
          <span className="text-gray-900">{user.isActive ? "Yes" : "No"}</span>
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