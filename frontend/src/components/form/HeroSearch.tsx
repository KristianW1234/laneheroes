import { Game } from '@/types/game';
import { useState } from 'react';

const initialFilters = {
        name: "",
        gender: "",
        gameId: "",
        title:"",
    };


export default function HeroSearch({
    games,
    onFilter
}:{
    games:Game[],
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

            <div className="flex flex-col">
                <label htmlFor="title" className="text-sm font-medium text-gray-700 mb-1">
                Title
                </label>
                <input
                type="text"
                id="title"
                value={filters.title}
                onChange={handleChange}
                placeholder="Search by title"
                className="input-text"
                />
            </div>

            {/* Another filter */}
            <div className="flex flex-col">
                <label htmlFor="gender" className="text-sm font-medium text-gray-700 mb-1">
                Gender
                </label>
                <select
                id="gender"
                className="input-text"
                value={filters.gender}
                onChange={handleChange}
                >
                <option value="">Select Gender</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="UNKNOWN">Unknown</option>
                </select>
            </div>

            <div className="flex flex-col">
                <label htmlFor="game" className="text-sm font-medium text-gray-700 mb-1">
                Game
                </label>
                <select
                    id="gameId"
                    className="input-text"
                    value={filters.gameId}
                    onChange={handleChange}
                    >
                    <option value="">Select Game</option>
                    {games.map((g) => (
                        <option key={g.id} value={g.id}>{g.gameName}</option>
                    ))}
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