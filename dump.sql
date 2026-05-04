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
    score INTEGER NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    players JSONB NOT NULL DEFAULT '[]'::JSONB,
    enemies JSONB NOT NULL DEFAULT '[]'::JSONB,
    inventory JSONB NOT NULL DEFAULT '[]'::JSONB,
    items JSONB NOT NULL DEFAULT '[]'::JSONB
);

CREATE TABLE scores (
    score_id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    player_id UUID REFERENCES players(player_id) ON DELETE CASCADE,
    value INTEGER NOT NULL,
    zombies_killed INT NOT NULL DEFAULT 0,
    nights_survived INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE items (
    item_id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    owner_id UUID REFERENCES players(player_id) DEFAULT NULL,
    inventory_id UUID REFERENCES inventories(inventory_id) DEFAULT NULL,
    durability float NOT NULL DEFAULT 100.0,
    impact float NOT NULL DEFAULT 0.0
);

CREATE TABLE inventories (
    inventory_id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    player_id UUID REFERENCES players(player_id) ON DELETE CASCADE,
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

