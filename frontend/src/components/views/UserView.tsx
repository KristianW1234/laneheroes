import { CardActionHandlers } from "@/types/actionhandlers";
import { User } from "@/types/user"; // Adjust path as needed


interface UserViewProps extends CardActionHandlers<User> {
  users: User[];
}

export default function UserView({ users, onEdit, onDelete, onDetail }: UserViewProps) {
  return (
    <table className="min-w-full table-auto border border-gray-300 text-sm">
      <thead className="bg-gray-100">
        <tr>
          <th className="px-4 py-2 border">ID</th>
          <th className="px-4 py-2 border">Username</th>
          <th className="px-4 py-2 border">Role</th>
          <th className="px-4 py-2 border">Email</th>
          <th className="px-4 py-2 border">Active</th>
          <th className="px-4 py-2 border">Actions</th>
        </tr>
      </thead>
      <tbody>
        {users.length > 0 ? (
          users.map((user) => (
            <tr key={user.id} className="hover:bg-gray-50">
              <td className="px-4 py-2 border text-center">{user.id}</td>
              <td className="px-4 py-2 border">{user.userName}</td>
              <td className="px-4 py-2 border">{user.userRole}</td>
              <td className="px-4 py-2 border">{user.userEmail}</td>
              <td className="px-4 py-2 border text-center">
                {user.isActive ? "Yes" : "No"}
              </td>
              <td className="px-4 py-2 border text-center space-x-2">
                <button
                    className="btn-base btn-blue"
                    onClick={() => onDetail(user)}
                    >
                    Detail
                    </button>
                    <button
                    className="btn-base btn-gray"
                    onClick={() => onEdit(user)}
                    >
                    Edit
                    </button>
                    <button
                    className="btn-base btn-red"
                    onClick={() => onDelete(user)}
                    >
                        Delete
                    </button>
              </td>
            </tr>
          ))
        ) : (
          <tr>
            <td colSpan={5} className="text-center py-4 text-gray-500">
              No users found.
            </td>
          </tr>
        )}
      </tbody>
    </table>
  );
}