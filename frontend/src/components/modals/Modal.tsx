import HeroDetail from "./hero/HeroDetail"; 
import GameDetail from "./game/GameDetail"; 
import PlatformDetail from "./platform/PlatformDetail"; 
import CompanyDetail from "./company/CompanyDetail"
import CallsignDetail from "./callsign/CallsignDetail"
import UserDetail from "./user/UserDetail"
import SkillDetail from "./skill/SkillDetail";

import HeroAdd from "./hero/HeroAdd"; 
import GameAdd from "./game/GameAdd"; 
import PlatformAdd from "./platform/PlatformAdd"; 
import CompanyAdd from "./company/CompanyAdd"; 
import CallsignAdd from "./callsign/CallsignAdd"; 
import UserAdd from "./user/UserAdd"; 
import SkillAdd from "./skill/SkillAdd";

import HeroEdit from "./hero/HeroEdit"; 
import GameEdit from "./game/GameEdit"; 
import PlatformEdit from "./platform/PlatformEdit"; 
import CompanyEdit from "./company/CompanyEdit"; 
import CallsignEdit from "./callsign/CallsignEdit"; 
import UserEdit from "./user/UserEdit"; 
import SkillEdit from "./skill/SkillEdit";

import ConfirmDelete from "./ConfirmDelete";
import ConfirmLogout from "./ConfirmLogout";

export default function Modal({
  modalType,
  modalProps,
  isCompact = false,
  onClose,
}: {
  modalType: string | null;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  modalProps: any;
  isCompact?: boolean;
  onClose: () => void;
}) {
  if (!modalType) return null;

  let content;

  switch (modalType) {
    case "hero-detail": 
      content = <HeroDetail {...modalProps} onClose={onClose} />;
      break;
    case "game-detail": 
      content = <GameDetail {...modalProps} onClose={onClose} />;
      break;
    case "platform-detail": 
      content = <PlatformDetail {...modalProps} onClose={onClose} />;
      break;
    case "company-detail": 
      content = <CompanyDetail {...modalProps} onClose={onClose} />;
      break;
    case "callsign-detail": 
      content = <CallsignDetail {...modalProps} onClose={onClose} />;
      break;
    case "user-detail": 
      content = <UserDetail {...modalProps} onClose={onClose} />;
      break;
    case "skill-detail": 
      content = <SkillDetail {...modalProps} onClose={onClose} />;
      break;
    case "hero-add":
      content = (
        <HeroAdd
          games={modalProps.games}
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "game-add":
      content = (
        <GameAdd
          companies={modalProps.companies}
          callsigns={modalProps.callsigns}
          platforms={modalProps.platforms}
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "platform-add":
      content = (
        <PlatformAdd
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "company-add":
      content = (
        <CompanyAdd
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "callsign-add":
      content = (
        <CallsignAdd
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "user-add":
      content = (
        <UserAdd
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "skill-add":
      content = (
        <SkillAdd
          heroes={modalProps.heroes}
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "hero-edit":
      content = (
        <HeroEdit
          hero={modalProps.hero}
          games={modalProps.games}
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "game-edit":
      content = (
        <GameEdit
          game={modalProps.game}
          platforms={modalProps.platforms}
          callsigns={modalProps.callsigns}
          companies={modalProps.companies}
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "platform-edit":
      content = (
        <PlatformEdit
          platform={modalProps.platform}
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "company-edit":
      content = (
        <CompanyEdit
          company={modalProps.company}
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "callsign-edit":
      content = (
        <CallsignEdit
          callsign={modalProps.callsign}
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "user-edit":
      content = (
        <UserEdit
          user={modalProps.user}
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;
    case "skill-edit":
      content = (
        <SkillEdit
          skill={modalProps.skill}
          heroes={modalProps.heroes}
          onClose={onClose}
          onSubmit={modalProps.onSubmit}
          onSuccess={modalProps.onSuccess}
        
        />
      );
      break;case "confirm-delete":
      content = <ConfirmDelete {...modalProps} onClose={onClose} />;
      break;
    case "confirm-logout":
      content = <ConfirmLogout onClose={onClose} onConfirm={modalProps.onSuccess}/>;
      break;
    default:
      return null;
  }

  return (
    <div className={`fixed inset-0 z-50 backdrop-blur-sm bg-black/40 flex items-center justify-center px-2 ${isCompact ? 'md:items-start md:pt-[15vh]' : ''}`}>
      <div className={`bg-white rounded shadow-lg p-6 w-full ${isCompact ? 'max-w-sm' : 'max-w-4xl'} h-auto flex flex-col gap-4 relative`}>
        {content}
      </div>
    </div>
  );
}