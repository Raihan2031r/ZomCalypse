CREATE EXTENSION IF NOT EXISTS pg_uuidv7;
SELECT uuid_generate_v7();

CREATE TABLE players (
    player_id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE games (
    game_id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    player_id UUID REFERENCES players(player_id) ON DELETE CASCADE,
    game_difficulty VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    zombies_killed INT NOT NULL DEFAULT 0,
    game_day INT NOT NULL DEFAULT 0,
    game_hour INT NOT NULL DEFAULT 0,
    game_minute INT NOT NULL DEFAULT 0,
    spent_score INT NOT NULL DEFAULT 0,
    player_hp REAL NOT NULL DEFAULT 100,
    player_energy REAL NOT NULL DEFAULT 100,
    player_satiation REAL NOT NULL DEFAULT 100,
    player_hydration REAL NOT NULL DEFAULT 100,
    player_position JSONB NOT NULL DEFAULT '{"x":0,"y":0}'::JSONB,
    current_weapon VARCHAR(100),
    inventory_items JSONB NOT NULL DEFAULT '[]'::JSONB,
    active_zombies JSONB NOT NULL DEFAULT '[]'::JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE scores (
    score_id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    player_id UUID REFERENCES players(player_id) ON DELETE CASCADE,
    total_score INTEGER NOT NULL,
    zombies_killed INT NOT NULL DEFAULT 0,
    days_survived INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_players_updated_at
BEFORE UPDATE ON players
FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

CREATE TRIGGER update_games_updated_at
BEFORE UPDATE ON games
FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

