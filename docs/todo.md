# AI Chat Frontend Todo

## Goal

Build an Android app that combines the best parts of ChatGPT and SillyTavern:

- ChatGPT style: smooth chat experience, editing, regenerate, export, stable multi-model chat.
- SillyTavern style: characters, world book, character cards, file context, strong roleplay features.
- Theme system: start with WeChat style, but keep the UI easy to switch later.

## Priorities

- `P0`: Daily-use chat experience and the most important core features.
- `P1`: SillyTavern-style roleplay features.
- `P2`: Better file handling for real use.
- `P3`: More polished ChatGPT-style experience.
- `P4`: Advanced features and system tools.
- `P5`: Theme system and UI style switching.

## Status Guide

- `Done`: Already implemented and usable.
- `In Progress`: Partly implemented, needs completion or polish.
- `Todo`: Not started yet.

## P0 Core Chat Experience

- `Done` Provider configuration
- `Done` OpenAI-compatible API support
- `Done` Multi-model switching
- `Done` Preset system
- `Done` Markdown rendering
- `Done` Model sync
- `Done` Settings page
- `Done` In-app update check and APK download flow
- `Done` Message edit
- `Done` Regenerate reply
- `Done` Delete message
- `Done` Chat export base support
- `In Progress` Multi-file context management
  Current state: multiple attachments exist, but file switch/control UX still needs work.
- `In Progress` Context control
  Current state: has history count and some limits, but still needs a clearer user-facing control panel and context preview.

## P1 Character And World Book

- `Done` Character system
- `Done` Character greeting
- `In Progress` Character tags and organization
- `Todo` Character card import from JSON
- `Todo` Character card import from PNG
- `Todo` Character avatar
- `Done` World book base system
- `Done` World book keyword match
- `Done` World book priority
- `In Progress` World book hit display in UI
- `Todo` Better world book rules
  Examples: regex trigger, token limit, better matching control.

## P2 File Features

- `Done` Basic text file upload as context
- `Todo` PDF support
- `Todo` DOCX support
- `In Progress` Attachment UX improvements
  Examples: clearer status, size display, extraction result display, better error messages.

## P3 Better ChatGPT-Like Experience

- `In Progress` Chat export polish
  Need cleaner entry points and better UX for Markdown/TXT/JSON export.
- `Todo` Search chats
- `Todo` Pin chats
- `Todo` Better rename/title flow
- `Todo` Message action menu polish
  Copy, edit, delete, regenerate, export from a clean menu.
- `Todo` Better Markdown support
  Examples: code block copy, tables, better long-text display.

## P4 Advanced And System Features

- `Todo` Tool calling
  Start simple: time, calculator, text tools.
- `Todo` MCP integration
- `Todo` Config import/export
  Provider, preset, character, world book, settings.
- `Todo` Data backup
- `Todo` Log export

## P5 Theme System

- `Todo` Theme architecture
  Do not hardcode WeChat style into every screen. Extract colors, spacing, bubble style, top bar style, list style.
- `Todo` First full theme: WeChat
- `Todo` Theme switching in settings
- `Todo` Future themes
  Examples: ChatGPT, Minimal, SillyTavern-inspired.

## Recommended Development Order

1. Message edit polish
2. Regenerate polish
3. Delete message polish
4. Chat export polish
5. Multi-file context management
6. Context control
7. Character card import
8. Character avatar
9. World book enhancement
10. PDF and DOCX support
11. Tool calling
12. Theme architecture
13. Config import/export and backup
14. MCP

## Working Agreement

- This file is the current long-term todo baseline.
- It can be refined, reordered, or changed at any time.
- After each meaningful feature is finished, update this file.
- When shipping app updates through GitHub Releases, remember:
  normal code push is not enough for in-app update detection.
  A new release needs a new version number and APK asset.
