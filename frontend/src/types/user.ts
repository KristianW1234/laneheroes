export interface User {
  id: number;
  userName: string;
  userPassword?: string;
  userRole: string;
  userEmail: string;
  isActive: boolean;
}