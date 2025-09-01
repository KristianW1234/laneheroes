import { Game } from './game';
import { Skill } from './skill';

export interface Hero {
  id: number;
  heroCode: string;
  heroName: string;
  heroTitle: string;
  heroGender: string;
  alternateName: string;
  imgIcon: string;
  displayByTitle: string;
  heroDescription: string;
  heroLore: string;
  game: Game;
  skills: Skill[];
}