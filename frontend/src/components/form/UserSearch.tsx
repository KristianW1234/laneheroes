import { useState } from 'react';

const initialFilters = {
        name: "",
        role: ""
        
    };


export default function UserSearch({
    onFilter
}:{
    onFilter: (filters: typeof initialFilters) => void;
}
    
){
    
    const [filters, setFilters] = useState(initialFilters);
    
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { id, value } = e.target;
        setFilters((prev) => ({ ...prev, [id]: value }));
    };

    const handleReset = () => {
        setFilters(initialFilters);
        onFilter(initialFilters);
    };


    return(
        <div className="flex flex-wrap gap-4 items-end">
            {/* Example filter field */}
            <div className="flex flex-col">
                <label htmlFor="name" className="text-sm font-medium text-gray-700 mb-1">
                Name
                </label>
                <input
                type="text"
                id="name"
                value={filters.name}
                onChange={handleChange}
                placeholder="Search by name"
                className="input-text"
                />
            </div>

            {/* Another filter */}
            <div className="flex flex-col">
                <label htmlFor="gender" className="text-sm font-medium text-gray-700 mb-1">
                Role
                </label>
                <select
                id="role"
                className="input-text"
                value={filters.role}
                onChange={handleChange}
                >
                <option value="">Select Role</option>
                <option value="ADMIN">Admin</option>
                <option value="MODERATOR">Moderator</option>
                <option value="USER">User</option>
                </select>
            </div>

            

            <div className="flex flex-col">
                <button 
                onClick={() => handleReset()}
                className="btn-base btn-red"
                >Reset</button>
            </div>

            <div className="flex flex-col">
                <button 
                onClick={() => onFilter(filters)}
                className="btn-base btn-blue"
                >Search</button>
            </div>

        
        </div>
    );
}