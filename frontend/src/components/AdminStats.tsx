import { Stats } from '@/types/stats';
import StatCard from '@/components/cards/StatCard';

type Props = {
  stats: Stats;
};

export default function AdminStats({ stats }: Props) {
  return (
    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
      <StatCard label="Companies" value={stats.companies} />
      <StatCard label="Callsigns" value={stats.callsigns} />
      <StatCard label="Platforms" value={stats.platforms} />
      <StatCard label="Games" value={stats.games} />
      <StatCard label="Heroes" value={stats.heroes} />
      <StatCard label="Users" value={stats.users} />
      <StatCard label="Skills" value={stats.skills} />
    </div>
  );
}
