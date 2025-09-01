type StatCardProps = {
  label: string;
  value: number;
};

export default function StatCard({ label, value }: StatCardProps) {
  return (
    <div className="p-4 bg-gray-100 rounded shadow">
      {value} {label}
    </div>
  );
}