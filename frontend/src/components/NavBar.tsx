'use client'

import Link from 'next/link'
import Dropdown from '@/components/Dropdown'

export interface MenuItem {
    title:string;
    route?: string;
    children?: MenuItem[];
}

const menuItems: MenuItem[] = [
    {
      title: "Company",
      children: [
        {
          title: "View",
          route: "#",
        },
        {
          title: "Add",
          route: "#",
        }
      ],
    },
    {
        title: "Callsign",
        children: [
          {
            title: "View",
            route: "#",
          },
          {
            title: "Add",
            route: "#",
          }
        ],
    },
    {
        title: "Platform",
        children: [
          {
            title: "View",
            route: "#",
          },
          {
            title: "Add",
            route: "#",
          }
        ],
    },
    {
        title: "Game",
        children: [
          {
            title: "View",
            route: "#",
          },
          {
            title: "Add",
            route: "#",
          }
        ],
    },
    {
        title: "Hero",
        children: [
          {
            title: "View",
            route: "#",
          },
          {
            title: "Add",
            route: "#",
          }
        ],
    },
    {
        title: "User",
        children: [
          {
            title: "View",
            route: "#",
          },
          {
            title: "Add",
            route: "#",
          }
        ],
    },
    {
        title: "Skill",
        children: [
          {
            title: "View",
            route: "#",
          },
          {
            title: "Add",
            route: "#",
          }
        ],
    }
    
    
  ];

interface NavBarProps {
  onViewSelect: (subject: string) => void;
  openModal: (type: string, props?: unknown) => void; 
}

export default function NavBar({ onViewSelect, openModal }: NavBarProps) {
  return (
    <nav className="relative bg-blue-400 border-t border-b border-gray-300 py-3">
      <ul className="flex justify-center gap-8">
        {menuItems.map((item) =>
          item.children ? (
            <Dropdown key={item.title} item={item} onViewSelect={onViewSelect} onModalOpen={openModal} />
          ) : (
            <Link key={item.title} href={item?.route || ''}>
              {item.title}
            </Link>
          )
        )}
      </ul>
    </nav>
  );
}
