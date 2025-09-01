import React, { useState } from 'react'

import { MenuItem } from '@/components/NavBar';

interface Props {
    item: MenuItem;
    onViewSelect: (subject: string) => void;
    onModalOpen: (type: string, props?: unknown) => void;
}

export default function Dropdown({ item, onViewSelect, onModalOpen }: Props) {
    const [isOpen, setIsOpen] = useState<boolean>(false);
    const menuItems = item?.children ? item.children : [];

    const toggle = () => {
        setIsOpen(old => !old);
    }

    const transClass = isOpen
        ?
        "flex"
        :
        "hidden";

    return (
        <>
            <div className="relative">
                <button
                    className="hover:text-white"
                    onClick={toggle}
                >{item.title}</button>
                <div className={`absolute top-8 z-30 w-[100px] flex flex-col py-4 bg-blue-400 rounded-md ${transClass}`}>
                {menuItems.map((subitem) => (
                    <button
                    key={subitem.title}
                    className="text-left hover:bg-zinc-300 hover:text-zinc-500 px-4 py-1"
                    onClick={() => {
                        toggle();
                        if (subitem.title === "View") {
                            onViewSelect(item.title);
                        } else if (subitem.title === "Add") {
                            onModalOpen(item.title); // e.g., "Hero"
                        }
                        
                        }}
                    >
                    {subitem.title}
                    </button>
                ))}
                </div>
            </div>
            {isOpen && <div className="fixed top-0 right-0 bottom-0 left-0 z-20" onClick={toggle}></div>}
            </>
    )
}