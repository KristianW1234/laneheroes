import { Company } from './company';
import { Callsign } from './callsign';
import { Platform } from './platform';

export interface Game {
    id: number;
    gameName: string;
    gameCode: string;
    imgIcon: string;
    callsign: Callsign;
    company: Company;
    platform: Platform;
  
}