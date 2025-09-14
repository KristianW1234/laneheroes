import HeroCard from "@/components/cards/HeroCard";
import { Hero } from "@/types/hero";
import GameCard from "@/components/cards/GameCard";
import { Game } from "@/types/game";
import PlatformCard from "@/components/cards/PlatformCard";
import { Platform } from "@/types/platform";
import CompanyCard from "@/components/cards/CompanyCard";
import { Company } from "@/types/company";
import CallsignCard from "@/components/cards/CallsignCard";
import { Callsign } from "@/types/callsign";
import { User } from "@/types/user";
import SkillCard from "@/components/cards/SkillCard";
import { Skill } from "@/types/skill";
import { useEffect, useState } from "react";
import { baseURL } from "@/utils/constants";
import toast from 'react-hot-toast';
import { useContext } from "react";
import { ReferenceDataContext } from "@/contexts/ReferenceDataContext";
import HeroSearch from "@/components/form/HeroSearch";
import GameSearch from "@/components/form/GameSearch";
import UserSearch from "@/components/form/UserSearch";
import SkillSearch from "@/components/form/SkillSearch";
import UserView from "@/components/views/UserView";
import { cardPerPage } from "@/utils/constants";
import { getFetchHeaders } from "@/utils/fetchHeaders";

interface ViewProps {
  subject: string;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  onModalOpen: (type: string, data?: any) => void;
  refreshStats: () => void;
  onRegisterRefresh?: (fn: () => void) => void;
}

export default function View({ subject, onModalOpen, onRegisterRefresh }: ViewProps) {
  const [heroes, setHeroes] = useState<Hero[]>([]);
  const [games, setGames] = useState<Game[]>([]);
  const [platforms, setPlatforms] = useState<Platform[]>([]);
  const [companies, setCompanies] = useState<Company[]>([]);
  const [callsigns, setCallsigns] = useState<Callsign[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [skills, setSkills] = useState<Skill[]>([]);
  const [page, setPage] = useState(0);
  const perPage = cardPerPage;
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [hasMore, setHasMore] = useState(true);
  const [totalPages, setTotalPages] = useState(0);
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [modalContent, setModalContent] = useState<string | null>(null);
  // eslint-disable-next-line @typescript-eslint/no-unused-vars, @typescript-eslint/no-explicit-any
  const [modalData, setModalData] = useState<any>(null);
  

  const { games: allGames } = useContext(ReferenceDataContext);
  const { heroes: allHeroes } = useContext(ReferenceDataContext);
  const { callsigns: allCallsigns } = useContext(ReferenceDataContext);
  const { companies: allCompanies } = useContext(ReferenceDataContext);
  const { platforms: allPlatforms } = useContext(ReferenceDataContext);
  
  const { skills: allSkills } = useContext(ReferenceDataContext);

  function renderCards() {
    let cards = null;
    console.log("Rendering cards for subject:", subject);
    switch (subject) {
        case "Hero":
          console.log("Rendering heroes:", heroes);
          cards = heroes.map((hero) => (
            <HeroCard
              key={hero.id}
              hero={hero}
              onDetail={() => handleDetail(hero)}
              onEdit={() => handleEdit(hero)}
              onDelete={() => handleDelete(hero)}
            />
          ));
          break;
        case "Game":
          cards = games.map((game) => (
            <GameCard
              key={game.id}
              game={game}
              onDetail={() => handleDetail(game)}
              onEdit={() => handleEdit(game)}
              onDelete={() => handleDelete(game)}
            />
          ));
          break;
        case "Platform":
          console.log("Rendering platforms:", platforms);
          cards = platforms.map((platform) => (
            <PlatformCard
              key={platform.id}
              platform={platform}
              onDetail={() => handleDetail(platform)}
              onEdit={() => handleEdit(platform)}
              onDelete={() => handleDelete(platform)}
            />
          ));
          break;
        case "Company":
          cards = companies.map((company) => (
            <CompanyCard
              key={company.id}
              company={company}
              onDetail={() => handleDetail(company)}
              onEdit={() => handleEdit(company)}
              onDelete={() => handleDelete(company)}
            />
          ));
          break;
        case "Callsign":
          cards = callsigns.map((callsign) => (
            <CallsignCard
              key={callsign.id}
              callsign={callsign}
              onDetail={() => handleDetail(callsign)}
              onEdit={() => handleEdit(callsign)}
              onDelete={() => handleDelete(callsign)}
            />
          ));
          break;
        case "Skill":
          console.log("Rendering skills:", skills);
          cards = skills.map((skill) => (
            <SkillCard
              key={skill.id}
              skill={skill}
              onDetail={() => handleDetail(skill)}
              onEdit={() => handleEdit(skill)}
              onDelete={() => handleDelete(skill)}
            />
          ));
          break;
      }
    return cards; // Add other subjects as needed
  }

  function renderTable(){
    return <UserView 
    users={users}
    onDetail={(user) => handleDetail(user)}
    onEdit={(user) => handleEdit(user)}
    onDelete={(user) => handleDelete(user)}
    />;
  }

  useEffect(() => {
    console.log('Skills from context:', skills);
  }, [skills]);
    
  

  useEffect(() => {
    setPage(0);
  }, [subject]);

  // 1. Define fetchData outside of useEffect
    const fetchData = async () => {
    try {
      let url = `${baseURL}/`+subject.toLowerCase()+`/`;
      if (subject === "Hero" || subject === "Game" || subject === "User" || subject === "Skill") {
        url += `search?page=${page}&size=${perPage}`;
      } else if (subject === "Platform" || subject === "Company" || subject === "Callsign") {
        url += `getAll`;
      }

      if (!url) return;

      const res = await fetch(url, {headers: getFetchHeaders()});
      const json = await res.json();
      console.log(`Response for ${subject} fetch:`, json);
      let result = json.data.content;

      if (subject === "Hero" || subject === "Game" || subject === "User" || subject === "Skill") {
        if (subject === "Hero") {
          setHeroes(result);
        } else if (subject === "Game") {
          setGames(result);
        } else if (subject === "User") {
          setUsers(result);
        } else if (subject === "Skill") {
          setSkills(result);
        }
        setHasMore(result.length === perPage);
        setTotalPages(json.data.totalPages);
      } else if (subject === "Platform" || subject === "Company" || subject === "Callsign"  ) {
        result = json.data;
        if (subject === "Company") {
          setCompanies(result);
        } else if (subject === "Platform") {
          setPlatforms(result);
        } else if (subject === "Callsign") {
          setCallsigns(result);
        }
        setHasMore(false);
        setTotalPages(1);
      }

      
      
    } catch (err) {
      console.error(`Failed to fetch ${subject}`, err);
    }
  };

  // 2. Reuse inside useEffect
  useEffect(() => {
    fetchData();
  }, [subject, page]);

  // 3. Register refresh function to MainPage
  useEffect(() => {
    onRegisterRefresh?.(fetchData);
  }, [onRegisterRefresh, subject, page]);

  

   

   const closeModal = () => {
    setModalContent(null);
    setModalData(null);
  };

  const refreshView = async () => {
    try {
      let res = null
      let json = null;
      let result = null;
      if (subject === "Hero" || subject === "Game" || subject === "User" || subject === "Skill") {
        res = await fetch(`${baseURL}/${subject.toLowerCase()}/search?page=${page}&size=${perPage}`, {headers: getFetchHeaders()});
        json = await res.json();
        result = json.data.content;
        if (result.length === 0 && page > 0) {
          setPage(page - 1);
          return;
        } 
        if (subject === "Hero") {
          setHeroes(result);
        } else if (subject === "Game") {
          setGames(result);
        } else if (subject === "User") {
          setUsers(result);
        } else if (subject === "Skill") {
          setSkills(result);
        }
        setHasMore(result.length === perPage);
        setTotalPages(json.data.totalPages);

      } else if (subject === "Platform" || subject === "Company" || subject === "Callsign") {
        console.log("Refreshing reference data for " + subject);
        res = await fetch(`${baseURL}/${subject.toLowerCase()}/getAll`, {headers: getFetchHeaders()});
        json = await res.json();
        result = json.data;
        if (subject === "Company") {
          setCompanies(result);
        } else if (subject === "Platform") {
          console.log("Setting platforms: " + JSON.stringify(result));
          setPlatforms(result);
        } else if (subject === "Callsign") {
          setCallsigns(result);
        }
        setHasMore(false);
        setTotalPages(1);
      }

    } catch (err){
      console.error("Failed to refresh view:", err);
    }
    
  }

  type HeroFilter = {
    name: string;
    title: string;
    gender: string;
    gameId: string;
  };

  type GameFilter = {
    name: string;
    companyId: string;
    platformId: string;
    callsignId: string;
  };

  type UserFilter = {
    name: string;
    role: string;
  };

  type SkillFilter = {
    name: string;
    heroId: string;
  };

  type FilterMap = {
    Hero: HeroFilter;
    Game: GameFilter;
    User: UserFilter;
    Skill: SkillFilter;
    // Add more subjects and their filters here
  };

  const doFilter = async <T extends keyof FilterMap>(
    subject: T,
    filters: FilterMap[T],
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    setItems: (data: any[]) => void
  ) => {
    const queryParams = new URLSearchParams();

    Object.entries(filters).forEach(([key, value]) => {
      if (value) {
        queryParams.append(key, value);
      }
    });

    try {
      
      const res = await fetch(
        `${baseURL}/${subject.toLowerCase()}/search?${queryParams}&page=${page}&size=${perPage}`, {headers: getFetchHeaders()}
      );
      const json = await res.json();
      const result = json.data.content;

      setPage(0); // always go back to first page
      setItems(result);
      setHasMore(result.length === perPage);
      setTotalPages(json.data.totalPages);
    } catch (err) {
      console.error(`Failed to filter ${subject.toLowerCase()}:`, err);
    }
  };


  const filterHeroes = (filters: HeroFilter) => {
    
    doFilter(
      "Hero",
      filters,
      setHeroes
      
    );
  };

  const filterGames = (filters: GameFilter) => {
    
    doFilter(
      "Game",
      filters,
      setGames
    );

  };

  const filterUsers = (filters: UserFilter) => {
    
    doFilter(
      "User",
      filters,
      setUsers
    );

  };

  const filterSkills = (filters: SkillFilter) => {
    
    doFilter(
      "Skill",
      filters,
      setSkills
    );

  };

  type acceptableItems = Hero | Game | Platform | Company | Callsign | User | Skill;
  

  const handleDetail = (item: acceptableItems) => {
    if (subject === "Hero") {
      onModalOpen("hero-detail", { hero: item });
    } else if (subject === "Game") {
      onModalOpen("game-detail", { game: item });
    } else if (subject === "Platform") {
      onModalOpen("platform-detail", { platform: item });
    } else if (subject === "Company") {
      onModalOpen("company-detail", { company: item });
    } else if (subject === "Callsign") {
      onModalOpen("callsign-detail", { callsign: item });
    } else if (subject === "User") {
      onModalOpen("user-detail", { user: item });
    } else if (subject === "Skill") {
      onModalOpen("skill-detail", { skill: item });
    }
    
  };

  const handleEdit = (item: acceptableItems) => {
    if (subject === "Hero") {
      onModalOpen("hero-edit", {
        hero: item as Hero,
        games: allGames,
        onSubmit: executeUpdate,
        onSuccess: refreshView,
      });
    } 
    
    if (subject === "Game") {
      onModalOpen("game-edit", {
        game: item as Game,
        platforms: allPlatforms,
        callsigns: allCallsigns,
        companies: allCompanies,
        onSubmit: executeUpdate,
        onSuccess: refreshView,
      });
    }

    if (subject === "Platform") {
      onModalOpen("platform-edit", {
        platform: item as Platform,
        onSubmit: executeUpdate,
        onSuccess: refreshView,
      });
    }

    if (subject === "Company") {
      onModalOpen("company-edit", {
        company: item as Company,
        onSubmit: executeUpdate,
        onSuccess: refreshView,
      });
    }

    if (subject === "Callsign") {
      onModalOpen("callsign-edit", {
        callsign: item as Callsign,
        onSubmit: executeUpdate,
        onSuccess: refreshView,
      });
    }

    if (subject === "User") {
      onModalOpen("user-edit", {
        user: item as User,
        onSubmit: executeUpdate,
        onSuccess: refreshView,
      });
    }

    if (subject === "Skill") {
      onModalOpen("skill-edit", {
        skill: item as Skill,
        heroes: allHeroes,
        onSubmit: executeUpdate,
        onSuccess: refreshView,
      });
    }
  };

  const executeUpdate = async (formData: FormData) => {
    try {
      const response = await fetch(
        `${baseURL}/${subject.toLowerCase()}/update`,
        {
          method: "PATCH",
          headers: getFetchHeaders(),
          body: formData,
        }
      );

      if (!response.ok) {
        throw new Error(`Update failed: ${response.status}`);
      }

      toast.success(subject + " updated!");
    } catch (error) {
      console.error(error);
      toast.error("Failed to update " + subject);
    }
  };

  const handleDelete = (item: acceptableItems) => {
    let itemName = "";
    if (subject === "Hero"){
      itemName = (item as Hero).heroName;
      const isUsedBySkill = allSkills.some(skill => skill.heroId === item.id);
      if (isUsedBySkill){
        toast.error("This hero cannot be deleted because it is used by one or more skills.");
        return;
      }

      if ((item as Hero).displayByTitle === 'Y') {
        itemName = (item as Hero).heroTitle;
      } 
    } else if (subject === "Game") {
      itemName = (item as Game).gameName;
      const isUsedByHero = allHeroes.some(hero => hero.game?.id === item.id);
      if (isUsedByHero) {
        toast.error("This game cannot be deleted because it is used by one or more heroes.");
        return;
      }
    } else if (subject === "Platform") {
      itemName = (item as Platform).platformName;
      const isUsedByGame = allGames.some(game => game.platform?.id === item.id);
      if (isUsedByGame) {
        toast.error("This platform cannot be deleted because it is used by one or more games.");
        return;
      }
    } else if (subject === "Company") {
      itemName = (item as Company).companyName;
      const isUsedByGame = allGames.some(game => game.company?.id === item.id);
      if (isUsedByGame) {
        toast.error("This company cannot be deleted because it is used by one or more games.");
        return;
      } 
    } else if (subject === "Callsign") {
      itemName = (item as Callsign).callsign;
      const isUsedByGame = allGames.some(game => game.callsign?.id === item.id);
      if (isUsedByGame) {
        toast.error("This callsign cannot be deleted because it is used by one or more games.");
        return;
      }
    } else if (subject === "User") {
      itemName = (item as User).userName;
    } else if (subject === "Skill") {
      itemName = (item as Skill).skillName;
    } 
    
    
    
    

    onModalOpen("confirm-delete", {
      type: subject,
      id: item.id,
      name: itemName,
      onDelete: executeDelete,
      onClose: closeModal,
      onSuccess: refreshView,
    });
  };

  const executeDelete = async (formData: FormData) => {
    try {
      const response = await fetch(
        `${baseURL}/${subject.toLowerCase()}/delete/${formData}`,
        {
          method: "DELETE",
          headers: getFetchHeaders(),
        }
      );

      if (!response.ok) {
        throw new Error(`Delete failed: ${response.status}`);
      }

      toast.success(subject + " deleted!");
    } catch (error) {
      console.error(error);
      toast.error("Failed to delete " + subject);
    }
  };

  return (
    <div className="min-h-screen p-4">
      {/* Sticky search section */}
      <div className="sticky top-0 bg-white z-10 p-4 shadow">
      <h1 className="text-2xl font-semibold mb-2">Filter {subject}</h1>
  
      {subject === "Hero" ? (
        <HeroSearch games={allGames} onFilter={filterHeroes} />
      ) : subject === "Game" ? (
        <GameSearch companies={allCompanies} platforms={allPlatforms} callsigns={allCallsigns} onFilter={filterGames} />
      ) : subject === "User" ? (
        <UserSearch onFilter={filterUsers} />
      ) : subject === "Skill" ? (
        <SkillSearch heroes={allHeroes} onFilter={filterSkills} />
      ) : (
        <div className="text-gray-600 italic">
          Filter feature not available for {subject}
        </div>
      )}
      
   </div>

      {/* Scrollable content */}
      <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        {subject === 'User' ? (
          renderTable()
        ) : (
          renderCards()
          )}
      </div>

      {/* Pagination controls */}
      <div className="flex justify-center mt-6 gap-4">
        <button
          className="btn-base btn-gray"
          disabled={page === 0}
          onClick={() => setPage((p) => p - 1)}
        >
          Previous
        </button>
        <span className="text-gray-600 mt-2">
          Page {page + 1} of {totalPages}
        </span>
        <button
          className="btn-base btn-blue"
          disabled={page + 1 >= totalPages}
          onClick={() => setPage((p) => p + 1)}
        >
          Next
        </button>
      </div>
    </div>
  );
}