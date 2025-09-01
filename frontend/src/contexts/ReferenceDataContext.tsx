import { createContext } from "react";
import { Hero } from "@/types/hero";
import { Game } from "@/types/game";
import { Company } from "@/types/company";
import { Platform } from "@/types/platform";
import { Callsign } from "@/types/callsign";
import { User } from "@/types/user";
import { Skill } from "@/types/skill";

export const ReferenceDataContext = createContext<{
  heroes: Hero[];
  games: Game[];
  companies: Company[];
  platforms: Platform[];
  callsigns: Callsign[];
  users: User[];
  skills: Skill[];
  
}>({
  heroes: [],
  games: [],
  companies: [],
  platforms: [],
  callsigns: [],
  users: [],
  skills: [],
  
});