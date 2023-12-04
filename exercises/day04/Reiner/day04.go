package main

import (
	"bufio"
	"fmt"
	"math"
	"os"

	"regexp"
	"strconv"

	//"strings"
	"time"
	//"golang.org/x/exp/slices"
	//"sort"
	//"utils"
	//"knothash"
	//"crypto/md5"
)

type Card struct {
	winning []int
	my      []int
	copies  int
}

const day = "04"
const dirName = "/home/reiner/Projects/aoc2023/advent-of-code-2023/exercises/day" + day + "/Reiner/"

func getNumberOfWins(c Card) int {
	var result int
	var wins int

	for _, m := range c.my {
		for _, w := range c.winning {
			if w == m {
				wins++
				break
			}
		}
	}

	result = wins

	return result
}

func part1(fn string) int {
	var result int

	cards := readInput(fn)

	for _, c := range cards {
		wins := getNumberOfWins((c))
		result += int(math.Pow(2, float64(wins-1)))
	}

	return result
}

func part2(fn string) int {
	var result int

	cards := readInput(fn)

	for i, c := range cards {
		wins := getNumberOfWins(c)

		for j := 1; j < wins+1; j++ {
			if i+j < len(cards) {
				cards[i+j].copies = cards[i+j].copies + cards[i].copies
			}
		}

	}

	for _, c := range cards {
		result += c.copies
	}

	return result
}

// tests
func runTest(part int, fn string, correct int) {
	var result int

	tStart := time.Now()
	if part == 1 {
		result = part1(dirName + fn)
	} else if part == 2 {
		result = part2(dirName + fn)
	}
	tEnd := time.Now()

	if result == correct {
		fmt.Println("Correct result for part", part, "test", fn, ":", result, "( in", tEnd.Sub(tStart), ")")
	} else {
		fmt.Println("Wrong result for part", part, "test", fn, ":", result, "(should be", correct, ")")
	}

}

func main() {
	fmt.Println("Hello from day", day)

	runTest(1, "input_p1t1.txt", 13)
	runTest(1, "input.txt", 28750)
	runTest(2, "input_p1t1.txt", 30)
	runTest(2, "input.txt", 10212704)
}

func readInput(fn string) []Card {
	var cards []Card

	cards = make([]Card, 0)

	regex1 := regexp.MustCompile(":|\\|")
	regex2 := regexp.MustCompile("[0-9]+")

	file, err := os.Open(fn)
	if err != nil {
		fmt.Println(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		var winning []int
		var my []int
		var card Card

		winning = make([]int, 0)
		my = make([]int, 0)

		line := scanner.Text()

		l := regex1.Split(line, -1)

		w := regex2.FindAllString(l[1], -1)
		for _, number := range w {
			n, _ := strconv.Atoi(number)
			winning = append(winning, n)
		}

		m := regex2.FindAllString(l[2], -1)
		for _, number := range m {
			n, _ := strconv.Atoi(number)
			my = append(my, n)
		}

		card.winning = winning
		card.my = my
		card.copies = 1

		cards = append(cards, card)

	}

	if err := scanner.Err(); err != nil {
		fmt.Println(err)
	}

	return cards
}
