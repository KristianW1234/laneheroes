'use client';

import { useEffect, useRef, useState } from 'react';
import AdminStats from '@/components/AdminStats';
import { Stats } from '@/types/stats';
import View from '@/components/View';
import NavBar from '@/components/NavBar';
import Modal from '@/components/modals/Modal';
import { Hero } from '@/types/hero';
import { Game } from '@/types/game';
import { Platform } from '@/types/platform';
import { Callsign } from '@/types/callsign';
import { Company } from '@/types/company';
import { User } from '@/types/user';
import { Skill } from '@/types/skill';
import axios from 'axios';
import toast from 'react-hot-toast';
import { baseURL } from '@/utils/constants';
import { ReferenceDataContext } from "@/contexts/ReferenceDataContext";
import { getAxiosHeaders } from '@/utils/axiosHeaders';
import { getFetchHeaders } from '@/utils/fetchHeaders';
import { isTokenExpired } from '@/utils/auth'

export default function MainPage() {
  const [stats, setStats] = useState<Stats>({
    heroes: 0,
    games: 0,
    users: 0,
    platforms: 0,
    callsigns: 0,
    companies: 0,
    skills: 0
  });

  const [currentView, setCurrentView] = useState<string | null>(null);

  // 🔹 Modal control state
  const [userName, setUserName] = useState<string | null>("Guest");
  const [modalType, setModalType] = useState<string | null>(null);
  const [modalProps, setModalProps] = useState<unknown>(null);
  const [allGames, setAllGames] = useState<Game[]>([]);
  const [allCompanies, setAllCompanies] = useState<Company[]>([]);
  const [allCallsigns, setAllCallsigns] = useState<Callsign[]>([]);
  const [allPlatforms, setAllPlatforms] = useState<Platform[]>([]);
  const [allHeroes, setAllHeroes] = useState<Hero[]>([]);
  const [allUsers, setAllUsers] = useState<User[]>([]);
  const [allSkills, setAllSkills] = useState<Skill[]>([]);

  const refreshViewRef = useRef<(() => void) | null>(null);

  const openModal = (type: string, props?: unknown) => {
    setModalType(type);
    setModalProps(props || {});
  };

  const refreshStats = async () => {
    console.log("RefreshStats is being called");
    const res = await fetch(`${baseURL}/api/admin/getStats`,{headers: getFetchHeaders()});
    const json = await res.json();
    setStats(json.data);
    setStats({
          heroes: json.data.heroes,
          games: json.data.games,
          users: json.data.users,
          platforms: json.data.platforms,
          callsigns: json.data.callsigns,
          companies: json.data.companies,
          skills: json.data.skills
          
        });
  };

  
  const openAddModal = (subject: string) => {
    switch (subject){
      case "Hero":
        openModal("hero-add", {
          games: allGames,
          onSubmit: handleAdd,
          onSuccess: () => {
            refreshStats();
            refreshViewRef.current?.();
          },
        });
        break;
      case "Game":
        openModal("game-add", {
          companies: allCompanies,
          callsigns: allCallsigns,
          platforms: allPlatforms,
          onSubmit: handleAdd,
          onSuccess: () => {
            refreshStats();
            refreshViewRef.current?.();
          },
        });
        break;
      case "Platform":
        openModal("platform-add", {
          onSubmit: handleAdd2,
          onSuccess: () => {
            refreshStats();
            refreshViewRef.current?.();
          },
        });
        break;
      case "Company":
        openModal("company-add", {
          onSubmit: handleAdd,
          onSuccess: () => {
            refreshStats();
            refreshViewRef.current?.();
          },
        });
        break;
      case "Callsign":
        openModal("callsign-add", {
          onSubmit: handleAdd2,
          onSuccess: () => {
            refreshStats();
            refreshViewRef.current?.();
          },
        });
        break;
      case "User":
        openModal("user-add", {
          onSubmit: handleAdd2,
          onSuccess: () => {
            refreshStats();
            refreshViewRef.current?.();
          },
        });
        break;
      case "Skill":
        openModal("skill-add", {
          heroes: allHeroes,
          onSubmit: handleAdd,
          onSuccess: () => {
            refreshStats();
            refreshViewRef.current?.();
          },
        });
        break;
      default:
        return;
    }
    
  };

  const closeModal = () => {
    setModalType(null);
    setModalProps(null);
  };

  const handleAdd = async (subject: string, formData : FormData) =>{
    try{
      console.log("Upload Add with formData: ", formData);
      for (const [key, value] of formData.entries()) {
        console.log(`${key}:`, value);
      }
      const response = await axios.post(`${baseURL}/`+subject.toLowerCase()+`/add`, formData, {headers: getAxiosHeaders()});
      console.log("Response from add: ", response);
      toast.success(subject+ " added!");
    }catch(error){
      console.error("Failed to add " + subject.toLowerCase() + ":", error);
      toast.error("Failed to add " + subject.toLowerCase() );
    }
  }

  type AddPayloads = {
    Platform: { platformName: string };
    Callsign: { callsign: string; callsignPlural: string };
    User: {userName: string; userPassword: string; userEmail: string; userRole: string; isActive: boolean};
    
  };

  type SubjectType = keyof AddPayloads; // 'platform' | 'callsign'

  const handleAdd2 = async <T extends SubjectType>(
    subject: T,
    data: AddPayloads[T]
  ) => {
    try {
      await axios.post(`${baseURL}/`+subject.toLowerCase()+`/add`, data, {headers: getAxiosHeaders()});
      toast.success(`${subject} added!`);
    } catch {
      toast.error(`Failed to add ${subject.toLowerCase()}.`);
    }
  };



  useEffect(() => {
    async function fetchAll() {
      try {
        const [statsRes, gamesRes, heroesRes, platformsRes, callsignsRes, companiesRes, usersRes, skillsRes] = await Promise.all([
          fetch(`${baseURL}/api/admin/getStats`, { headers: getFetchHeaders() }),
          fetch(`${baseURL}/game/getAll`,        { headers: getFetchHeaders() }),
          fetch(`${baseURL}/hero/getAll`,        { headers: getFetchHeaders() }),
          fetch(`${baseURL}/platform/getAll`,    { headers: getFetchHeaders() }),
          fetch(`${baseURL}/callsign/getAll`,    { headers: getFetchHeaders() }),
          fetch(`${baseURL}/company/getAll`,     { headers: getFetchHeaders() }),
          fetch(`${baseURL}/user/getAll`,        { headers: getFetchHeaders() }),
          fetch(`${baseURL}/skill/getAll`,        { headers: getFetchHeaders() })
        ]);

        const statsJson = await statsRes.json();
        const gamesJson = await gamesRes.json();
        const heroesJson = await heroesRes.json();
        const platformsJson = await platformsRes.json();
        const callsignsJson = await callsignsRes.json();
        const companiesJson = await companiesRes.json();
        const usersJson = await usersRes.json();
        const skillsJson = await skillsRes.json();

        setStats({
          heroes: statsJson.data.heroes,
          games: statsJson.data.games,
          users: statsJson.data.users,
          platforms: statsJson.data.platforms,
          callsigns: statsJson.data.callsigns,
          companies: statsJson.data.companies,
          skills: statsJson.data.skills
        });

        setAllGames(gamesJson.data);
        setAllHeroes(heroesJson.data);
        setAllCompanies(companiesJson.data);
        setAllCallsigns(callsignsJson.data);
        setAllPlatforms(platformsJson.data);
        setAllUsers(usersJson.data);
        setAllSkills(skillsJson.data);
        console.log("Skills fetched:", skillsJson.data);
        
      } catch (err) {
        console.error('Failed to fetch:', err);
      }
    }

    fetchAll();
  }, []);

  const logoutPrompt = () => {

      openModal("confirm-logout", {
          onClose: closeModal,
          onSuccess: handleLogout,
        });
  }

  const resetCurrentView = () => {
    setCurrentView(null);
    refreshStats();
  }

  

  const handleLogout = () => {
    localStorage.removeItem("token"); 
    localStorage.removeItem("user"); 
    window.location.href = "/login";
  };

  useEffect(() => {
    const stored = localStorage.getItem("user");
    if (stored) {
      const loggedUser = JSON.parse(stored);
      if (loggedUser?.userName) {
        setUserName(loggedUser.userName);
      }
    }
  }, []);

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token || isTokenExpired(token)) {
      localStorage.clear();
      window.location.href = "/login";
    }
  }, []);

  useEffect(() => {
  console.log("✅ allSkills updated:", allSkills);
}, [allSkills]);

  return (
    <div className="mx-auto relative">
      <div className="-mx-6 bg-blue-400 border-b border-gray-300">
        <NavBar
          onViewSelect={setCurrentView}
          openModal={openAddModal}
        />
      </div>

      <div className="flex justify-between items-center mb-4">
        <p>
          Welcome! You are logged in as{" "}
          <span className="font-semibold text-blue-700">
            {userName}
          </span>
        </p>
        <div className="flex gap-2">
          <button
            onClick={resetCurrentView}
            className="btn-base btn-blue"
          >
            Stats
          </button>
          <button
            onClick={logoutPrompt}
            className="btn-base btn-red"
          >
            Logout
          </button>
        </div>
      </div>

      <div className="bg-white shadow rounded p-4 w-full">
        {currentView ? (
          <ReferenceDataContext.Provider
          value={{
            heroes: allHeroes,
            games: allGames,
            companies: allCompanies,
            callsigns: allCallsigns,
            platforms: allPlatforms,
            users: allUsers,
            skills: allSkills,
          }}
        >
            <View 
            subject={currentView} 
            onModalOpen={openModal} 
            refreshStats={refreshStats} 
            onRegisterRefresh={(fn) => {
              refreshViewRef.current = fn;
            }}/>

        </ReferenceDataContext.Provider>
          
        ) : (
          <AdminStats stats={stats} />
        )}
      </div>

      {/* 🔹 Render Modal */}
      {modalType && (
      <Modal
        modalType={modalType}
        modalProps={modalProps}
        onClose={closeModal}
        {...((modalType === "confirm-delete" || modalType === "confirm-logout") && { isCompact: true })}
      />
    )}
    </div>
  );
}