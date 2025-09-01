import { Company } from '@/types/company';
import { Platform } from '@/types/platform';
import { Callsign } from '@/types/callsign';
import { useState } from 'react';

const initialFilters = {
        name: "",
        companyId: "",
        platformId: "",
        callsignId:"",
    };


export default function GameSearch({
    companies,
    platforms,
    callsigns,
    onFilter
}:{
    companies:Company[],
    platforms:Platform[],
    callsigns:Callsign[],
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
                <label htmlFor="company" className="text-sm font-medium text-gray-700 mb-1">
                Company
                </label>
                <select
                    id="companyId"
                    className="input-text"
                    value={filters.companyId}
                    onChange={handleChange}
                    >
                    <option value="">Select Company</option>
                    {companies.map((c) => (
                        <option key={c.id} value={c.id}>{c.companyName}</option>
                    ))}
                    </select>
            </div>

            <div className="flex flex-col">
                <label htmlFor="platform" className="text-sm font-medium text-gray-700 mb-1">
                Company
                </label>
                <select
                    id="platformId"
                    className="input-text"
                    value={filters.platformId}
                    onChange={handleChange}
                    >
                    <option value="">Select Platform</option>
                    {platforms.map((p) => (
                        <option key={p.id} value={p.id}>{p.platformName}</option>
                    ))}
                    </select>
            </div>

            <div className="flex flex-col">
                <label htmlFor="callsign" className="text-sm font-medium text-gray-700 mb-1">
                Callsign
                </label>
                <select
                    id="callsignId"
                    className="input-text"
                    value={filters.callsignId}
                    onChange={handleChange}
                    >
                    <option value="">Select Callsign</option>
                    {callsigns.map((c) => (
                        <option key={c.id} value={c.id}>{c.callsign}/{c.callsignPlural}</option>
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