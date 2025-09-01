const isDocker = process.env.NEXT_PUBLIC_USE_DOCKER === "true"; 
export const baseURL = isDocker 
  ? process.env.NEXT_PUBLIC_API_BASE_URL_DOCKER || "http://backend:8080/laneHeroes" 
  : process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080/laneHeroes"; 
export const cardPerPage = 12;