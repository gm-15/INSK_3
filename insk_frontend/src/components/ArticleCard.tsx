// ArticleCard.tsx

interface ArticleCardProps {
  title: string;
  summary: string;
}

export default function ArticleCard({ title, summary }: ArticleCardProps) {
  return (
    // Tailwind CSS 클래스를 적용하여 스타일링
    <div className="bg-gray-800 p-4 rounded-lg border border-gray-700 mb-4">
      <h2 className="text-xl font-bold text-white mb-2">{title}</h2>
      <p className="text-gray-400">{summary}</p>
    </div>
  );
}